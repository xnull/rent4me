package bynull.realty.services.impl.socialnet.fb;

import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.components.text.Porter;
import bynull.realty.components.text.RoomCountParser;
import bynull.realty.config.Config;
import bynull.realty.converters.FacebookPageModelDTOConverter;
import bynull.realty.converters.FacebookPostModelDTOConverter;
import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.dao.external.FacebookScrapedPostRepository;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.services.api.FacebookService;
import bynull.realty.util.LimitAndOffset;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 02/01/15.
 */
@Service
@Slf4j
public class FacebookServiceImpl implements FacebookService, InitializingBean {
    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();
    @Resource
    Config config;
    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;
    @Resource
    FacebookScrapedPostRepository facebookScrapedPostRepository;
    @Resource
    FacebookHelperComponent facebookHelperComponent;

    Porter porter;

    //converters
    @Resource
    FacebookPostModelDTOConverter facebookPostConverter;

    @Resource
    FacebookPageModelDTOConverter facebookPageConverter;

    @Resource
    MetroModelDTOConverter metroConverter;

    @PersistenceContext
    EntityManager em;
    @Resource
    MetroRepository metroRepository;
    @Resource
    MetroTextAnalyzer metroTextAnalyzer;

    RoomCountParser roomCountParser;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
        roomCountParser = RoomCountParser.getInstance();
    }

    @Transactional
    @Override
    public void scrapNewPosts() {
        List<FacebookPageToScrap> fbPages = facebookPageToScrapRepository.findAll();
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());

        em.clear();//detach all instances
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (FacebookPageToScrap fbPage : fbPages) {
            List<FacebookScrapedPost> newest = facebookScrapedPostRepository.findByExternalIdNewest(fbPage.getExternalId(), getLimit1Offset0());

            Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getCreated();

            try {
                List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOs = facebookHelperComponent.loadPostsFromPage(fbPage.getExternalId(), maxPostsAgeToGrab);
                List<FacebookScrapedPost> byExternalIdIn = !facebookPostItemDTOs.isEmpty() ? facebookScrapedPostRepository.findByExternalIdIn(facebookPostItemDTOs.stream()
                                .map(FacebookHelperComponent.FacebookPostItemDTO::getId)
                                .collect(Collectors.toList())
                ) : Collections.emptyList();
                Set<String> ids = byExternalIdIn.stream()
                        .map(FacebookScrapedPost::getExternalId)
                        .collect(Collectors.toSet());

                List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOsToPersist = facebookPostItemDTOs
                        .stream()
                        .filter(i -> !ids.contains(i.getId()))
                        .collect(Collectors.toList());

                em.clear();

                final Date threeMonthsAgo = new DateTime().minusMonths(3).toDate();

                Iterable<List<FacebookHelperComponent.FacebookPostItemDTO>> partitions = Iterables.partition(facebookPostItemDTOsToPersist, 20);
                for (List<FacebookHelperComponent.FacebookPostItemDTO> partition : partitions) {
                    FacebookPageToScrap page = new FacebookPageToScrap(fbPage.getId());
                    for (FacebookHelperComponent.FacebookPostItemDTO postItemDTO : partition) {
                        FacebookScrapedPost post = postItemDTO.toInternal();
                        if (post.getCreated() != null && post.getCreated().before(threeMonthsAgo)) {
                            log.info("Skipping post that's older than 3 months old: [{}]");
                            continue;
                        }
                        post.setFacebookPageToScrap(page);
                        Set<MetroEntity> matchedMetros = matchMetros(metros, post.getMessage());
                        post.setMetros(matchedMetros);
                        Integer roomCount = roomCountParser.findRoomCount(post.getMessage());
                        post.setRoomCount(roomCount);
                        facebookScrapedPostRepository.save(post);
                    }
                    em.flush();
                    em.clear();
                }
                em.flush();
            } catch (Exception e) {
                log.error("Failed to parse [" + fbPage.getExternalId() + "]", e);
            }
        }
    }

    private PageRequest getLimit1Offset0() {
        return new PageRequest(0, 1);
    }

    @Override
    public void syncElasticSearchWithDB() {
        PutMethod method = new PutMethod(config.getEsConfig().getEsConnectionUrl() + "/_river/" + config.getEsConfig().getRiver() + "/_meta");

        try {
            DbConfig dbConfig = new DbConfig();
            dbConfig.type = "jdbc";
            dbConfig.jdbc.user = config.getEsConfig().getDbUsername();
            dbConfig.jdbc.password = config.getEsConfig().getDbPassword();
            dbConfig.jdbc.url = config.getEsConfig().getDbJdbcUrl();

            //execute incremental updates
            dbConfig.jdbc.sql.add(new DbConfig.Jdbc.Sql(
                    "select * from facebook_scraped_posts where imported_dt > (select last_run from es_river_stats)",
//                    ImmutableList.of("$river.state.last_active_begin")
                    ImmutableList.of()
            ));
            dbConfig.jdbc.sql.add(new DbConfig.Jdbc.Sql(
                    "update es_river_stats set last_run=now()",
                    ImmutableList.of(),
                    true
            ));
            dbConfig.jdbc.index = config.getEsConfig().getIndex();
            dbConfig.jdbc.type = config.getEsConfig().getType();
            dbConfig.jdbc.interval = 30;

            String value = jacksonObjectMapper.writeValueAsString(dbConfig);

            log.info("DB config for ES sync: [{}]", value);

            method.setRequestEntity(new StringRequestEntity(value, null, "UTF-8"));
            int responseCode = httpManager.executeMethod(method);
            String body = method.getResponseBodyAsString();

            log.info("Response code for ES sync: [{}]", responseCode);
            log.info("Response body for ES sync: [{}]", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            method.releaseConnection();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPostDTO> findFBPosts(String text, boolean withSubway, Set<RoomCount> roomsCount, LimitAndOffset limitAndOffset, FindMode findMode) {
        Assert.notNull(text);
        Assert.notNull(roomsCount);

        return findPosts(text, withSubway, roomsCount, limitAndOffset, findMode);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPostDTO> findRenterPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset) {
        Assert.notNull(text);

//        if (StringUtils.trimToEmpty(text).isEmpty()) return Collections.emptyList();

        FindQuery.BoolQuery typeQuery = new FindQuery.BoolQuery(
                null,
                null,
                ImmutableList.of(
                        new FindQuery.MatchQueryByMessage(new MessageMatch("сдаю")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("сдам")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("отдам")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("отдаю")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("сдается"))
                )
        );

//        return findPosts(text, withSubway, limitAndOffset, typeQuery);
        return findPosts(text, withSubway, Collections.emptySet(), limitAndOffset, FindMode.RENTER);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPostDTO> findLessorPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset) {
        Assert.notNull(text);

//        if (StringUtils.trimToEmpty(text).isEmpty()) return Collections.emptyList();

        FindQuery.BoolQuery typeQuery = new FindQuery.BoolQuery(
                null,
                null,
                ImmutableList.of(
                        new FindQuery.MatchQueryByMessage(new MessageMatch("сниму")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("снимаю")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("снять")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("снял")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("возьму")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("взял")),
                        new FindQuery.MatchQueryByMessage(new MessageMatch("взять"))
                )
        );

//        return findPosts(text, withSubway, limitAndOffset, typeQuery);
        return findPosts(text, withSubway, Collections.emptySet(), limitAndOffset, FindMode.LESSOR);
    }

    private List<FacebookPostDTO> findPosts(String text, boolean withSubway, Set<RoomCount> roomsCount, LimitAndOffset limitAndOffset, FindMode findMode) {
        Assert.notNull(roomsCount);
        text = StringUtils.trimToEmpty(text);
        final List<String> keyWords;

        switch (findMode) {
            case LESSOR:
                keyWords = Arrays.asList("сниму", "снимаю", "снять", "снял", "возьму", "взял", "взять");
                break;
            case RENTER:
                keyWords = Arrays.asList("сдаю", "сдам", "отдам", "отдаю", "отдается");
                break;
            default:
                throw new UnsupportedOperationException("Find mode " + findMode + " not supported");
        }


        String findModeJPQL = keyWords
                .stream()
                .map(word -> " ( lower(p.message) like '" + ilike(word) + "' ) ")
                .collect(Collectors.joining(" OR "));

        String searchText = text.isEmpty() ? "" : text.length() > 5 ? porter.stem(text) : text;

        String qlString = "select p from FacebookScrapedPost p where (" + findModeJPQL + ")" +
                (!searchText.isEmpty() ? " AND lower(p.message) like :msg " : "") +
                (withSubway ? " AND p.metros IS NOT EMPTY " : "") +
                (!roomsCount.isEmpty() ? " AND p.roomCount IN (:roomCounts) " : "") +
                " ORDER BY p.created DESC";
        TypedQuery<FacebookScrapedPost> query = em.createQuery(qlString, FacebookScrapedPost.class);

        if (!searchText.isEmpty()) {
            query.setParameter("msg", ilike(searchText));
        }

        if (!roomsCount.isEmpty()) {
            Set<Integer> values = roomsCount.stream().map(rc -> Integer.valueOf(rc.value)).collect(Collectors.toSet());
            query.setParameter("roomCounts", values);
        }

        List<FacebookScrapedPost> resultList = query
                .setFirstResult(limitAndOffset.offset)
                .setMaxResults(limitAndOffset.limit)
                .getResultList();

        return facebookPostConverter.toTargetList(resultList);
    }

    private String ilike(String text) {
        return !text.isEmpty() ? "%" + text.toLowerCase() + "%" : "%";
    }

    private List<FacebookPostDTO> findPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset, FindQuery.BoolQuery typeQuery) {
        String index = config.getEsConfig().getIndex();
//        index = "prod_fb_posts";

        PostMethod method = new PostMethod(config.getEsConfig().getEsConnectionUrl() + "/" + index + "/_search");
        ;
//        PostMethod method = new PostMethod("http://rent4.me:9200/"+ index +"/_search");;
        List<FindQuery.Query> searchQueries = Arrays.asList(StringUtils.split(text))
                .stream()
                .map(e -> new FindQuery.FuzzyLikeThisQueryByMessage(new FindQuery.FuzzyLikeThisQueryByMessage.Message(e)))
                .collect(Collectors.toCollection(ArrayList::new));

        if (withSubway) {
            searchQueries.add(new FindQuery.BoolQuery(null, null,
                    ImmutableList.of(
                            new FindQuery.MatchQueryByMessage(new MessageMatch("м.")),
                            new FindQuery.FuzzyLikeThisQueryByMessage(new FindQuery.FuzzyLikeThisQueryByMessage.Message("метро"))
                    )));
        }

        try {
            FindQuery query = new FindQuery();
            query.setFrom(limitAndOffset.offset);
            query.setSize(limitAndOffset.limit);

            query.setQuery(
                    new FindQuery.BoolQuery(
                            ImmutableList.of(
                                    new FindQuery.BoolQuery(
                                            searchQueries,
                                            null,
                                            null
                                    ),
                                    typeQuery
                            ),
                            null,
                            null
                    ))
            ;
            query.setSort(ImmutableList.of(
                    new FindQuery.SortByExternalCreatedDt(new FindQuery.SortParams(FindQuery.SortParams.Order.desc, FindQuery.SortParams.Mode.min))
            ));

            String value = jacksonObjectMapper.writeValueAsString(query);

            log.info("DB config for ES sync: [{}]", value);

            method.setRequestEntity(new StringRequestEntity(value, null, "UTF-8"));
            int responseCode = httpManager.executeMethod(method);
            String body = method.getResponseBodyAsString();

            log.info("Response code for ES sync: [{}]", responseCode);
            log.info("Response body for ES sync: [{}]", body);

            ESResponse response = jacksonObjectMapper.readValue(body, ESResponse.class);

            List<FacebookPostDTO> resultList = response.hits
                    .hits.stream()
                    .map(e -> e.source)
                    .map(e -> {
                        FacebookPostDTO dto = new FacebookPostDTO();
                        dto.setMessage(e.message);
                        dto.setLink(e.link);
                        dto.setImageUrls(e.picture != null ? Collections.singletonList(e.picture) : Collections.emptyList());
                        dto.setCreated(e.createdDt);
                        dto.setUpdated(e.updatedDt);
                        return dto;
                    })
                    .collect(Collectors.toList());

            return resultList;


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            method.releaseConnection();
        }
    }

    @Transactional
    @Override
    public void save(FacebookPageDTO pageDTO) {
        FacebookPageToScrap entity = facebookPageConverter.toSourceType(pageDTO);
        if (entity.getId() != null) {
            FacebookPageToScrap found = facebookPageToScrapRepository.findOne(entity.getId());
            found.setLink(entity.getLink());
            found.setExternalId(entity.getExternalId());
            facebookPageToScrapRepository.saveAndFlush(found);
        } else {
            facebookPageToScrapRepository.saveAndFlush(entity);
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        facebookPageToScrapRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPageDTO> listAllPages() {
        return facebookPageToScrapRepository.findAll()
                .stream()
                .sorted(new Comparator<FacebookPageToScrap>() {
                    @Override
                    public int compare(FacebookPageToScrap o1, FacebookPageToScrap o2) {
                        return o2.getUpdated().compareTo(o1.getUpdated());
                    }
                })
                .map(facebookPageConverter::toTargetType)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public FacebookPageDTO findPageById(long fbPageId) {
        return facebookPageConverter.toTargetType(facebookPageToScrapRepository.findOne(fbPageId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPostDTO> findPosts(PageRequest pageRequest) {
        return facebookPostConverter.toTargetList(facebookScrapedPostRepository.findAll(pageRequest).getContent());
    }

    @Transactional(readOnly = true)
    @Override
    public long countOfPages() {
        return facebookScrapedPostRepository.count();
    }

    @Transactional
    @Override
    public void reparseExistingFBPosts() {
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());
        int countOfMatchedPosts = 0;
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "id");
        Page<FacebookScrapedPost> postsPage = facebookScrapedPostRepository.findAll(pageable);
        long total = facebookScrapedPostRepository.count();
        boolean hasNext = false;
        do {
            List<FacebookScrapedPost> posts = postsPage.getContent();
            for (FacebookScrapedPost post : posts) {
                Set<MetroEntity> matchedMetros = matchMetros(metros, post.getMessage());
                post.setMetros(matchedMetros);
                Integer roomCount = roomCountParser.findRoomCount(post.getMessage());
                post.setRoomCount(roomCount);
                if (!matchedMetros.isEmpty()) {
                    countOfMatchedPosts++;
                }
                post = facebookScrapedPostRepository.save(post);
            }
            log.info("Processed page #[{}]", pageable);
            hasNext = postsPage.hasNext();
            if (hasNext) {
                pageable = postsPage.nextPageable();
                em.flush();
                em.clear();
                postsPage = facebookScrapedPostRepository.findAll(pageable);
            }
        } while (hasNext);
        log.info("Total count of matched posts to metro stations: [{}]. Total posts: [{}]", countOfMatchedPosts, total);
    }

    private Set<MetroEntity> matchMetros(List<MetroDTO> metros, String message) {
        Set<MetroEntity> matchedMetros = new HashSet<>();
        for (MetroDTO metro : metros) {
            if (metroTextAnalyzer.matches(message, metro.getStationName())) {
//                log.info("Post #matched to metro #[] ({})", metro.getId(), metro.getStationName());

                matchedMetros.add(metroRepository.findOne(metro.getId()));

            }
        }
        return matchedMetros;
    }

    public static class DbConfig {
        public String type;
        public Jdbc jdbc = new Jdbc();

        public static class Jdbc {
            public String url;
            public String user;
            public String password;
            public List<Sql> sql = new ArrayList<>();
            public String index;
            public String type;
            public long interval;

            public static class Sql {
                public String statement;
                public List<String> parameter;
                public boolean write;

                public Sql(String statement, List<String> parameter) {
                    this(statement, parameter, false);
                }

                public Sql(String statement, List<String> parameter, boolean write) {
                    this.statement = statement;
                    this.parameter = parameter;
                    this.write = write;
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public static class ESResponse {
        @JsonProperty("hits")
        HitSuperEntry hits;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class HitSuperEntry {
            @JsonProperty("total")
            private long total;
            @JsonProperty("hits")
            private List<HitEntry> hits;

            @JsonIgnoreProperties(ignoreUnknown = true)
            @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
            public static class HitEntry {
                @JsonProperty("_source")
                FacebookPostESJson source;
            }
        }
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @Getter
    @Setter
    public static class FindQuery {
        @JsonProperty("query")
        private Query query;
        @JsonProperty("from")
        private int from;
        @JsonProperty("size")
        private int size;

        @JsonProperty("sort")
        private List<Sort> sort;

        public static interface Query {

        }

        public static interface Sort {

        }

        public static interface BoolSubQuery extends Query {

        }

        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class SortParams {
            @JsonProperty("order")
            private final Order order;
            @JsonProperty("mode")
            private final Mode mode;

            public SortParams(Order order, Mode mode) {
                this.order = order;
                this.mode = mode;
            }

            public static enum Mode {
                min, max, sum, avg
            }

            public static enum Order {
                asc, desc
            }
        }

        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class SortByExternalCreatedDt implements Sort {
            @JsonProperty("ext_created_dt")
            private final SortParams extCreatedDt;

            public SortByExternalCreatedDt(SortParams extCreatedDt) {
                this.extCreatedDt = extCreatedDt;
            }
        }

        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class BoolQuery implements Query {
            @JsonProperty("bool")
            private final BoolWrapper bool;

            public BoolQuery(List<Query> must, List<Query> mustNot, List<Query> should) {
                this.bool = new BoolWrapper(must, mustNot, should);
            }


            @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
            @Getter
            public static class BoolWrapper {
                @JsonProperty("must")
                private final List<Query> must;
                @JsonProperty("must_not")
                private final List<Query> mustNot;
                @JsonProperty("should")
                private final List<Query> should;

                public BoolWrapper(List<Query> must, List<Query> mustNot, List<Query> should) {
                    this.must = must;
                    this.mustNot = mustNot;
                    this.should = should;
                }
            }
        }

        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class FuzzyLikeThisQueryByMessage implements Query {
            @JsonProperty("fuzzy_like_this")
            private final Message fuzzy;

            public FuzzyLikeThisQueryByMessage(Message fuzzy) {
                this.fuzzy = fuzzy;
            }

            @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
            @Getter
            public static class Message {
                @JsonProperty("fields")
                private final List<String> fields = ImmutableList.of("message");
                @JsonProperty("like_text")
                private final String likeText;

                public Message(String likeText) {
                    this.likeText = likeText;
                }
            }
        }

        @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        @Getter
        public static class MatchQueryByMessage implements Query {
            @JsonProperty("match")
            private final MessageMatch match;

            public MatchQueryByMessage(MessageMatch match) {
                this.match = match;
            }


        }

    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @Getter
    public static class MessageMatch {
        @JsonProperty("message")
        private final String message;

        public MessageMatch(String message) {
            this.message = message;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public static class FacebookPostESJson {
        @JsonProperty("external_id")
        private String id;
        @JsonProperty("message")
        private String message;
        @JsonProperty("link")
        private String link;
        @JsonProperty("picture")
        private String picture;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        @JsonProperty("ext_created_dt")
        private Date createdDt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        @JsonProperty("ext_updated_dt")
        private Date updatedDt;
    }
}
