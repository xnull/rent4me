package bynull.realty.services.impl;

import bynull.realty.components.FacebookHelperComponent;
import bynull.realty.config.Config;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.dao.external.FacebookScrapedPostRepository;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.services.api.FacebookScrapingPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 02/01/15.
 */
@Service
public class FacebookScrapingPostServiceImpl implements FacebookScrapingPostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookScrapingPostServiceImpl.class);

    @Resource
    Config config;

    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;

    @Resource
    FacebookScrapedPostRepository facebookScrapedPostRepository;

    @Resource
    FacebookHelperComponent facebookHelperComponent;

    @PersistenceContext
    EntityManager em;

    @Transactional
    @Override
    public void scrapNewPosts() {
        List<FacebookPageToScrap> fbPages = facebookPageToScrapRepository.findAll();
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

                Iterable<List<FacebookHelperComponent.FacebookPostItemDTO>> partitions = Iterables.partition(facebookPostItemDTOsToPersist, 20);
                for (List<FacebookHelperComponent.FacebookPostItemDTO> partition : partitions) {
                    FacebookPageToScrap page = new FacebookPageToScrap(fbPage.getId());
                    for (FacebookHelperComponent.FacebookPostItemDTO postItemDTO : partition) {
                        FacebookScrapedPost post = postItemDTO.toInternal();
                        post.setFacebookPageToScrap(page);
                        facebookScrapedPostRepository.save(post);
                    }
                    em.flush();
                    em.clear();
                }
                em.flush();
            } catch (Exception e) {
                LOGGER.error("Failed to parse ["+fbPage.getExternalId()+"]", e);
            }
        }
    }

    private PageRequest getLimit1Offset0() {
        return new PageRequest(0, 1);
    }

    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};

    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public static class DbConfig {
        public String type;
        public Jdbc jdbc=new Jdbc();

        public static class Jdbc {
            public String url;
            public String user;
            public String password;
            public List<Sql> sql=new ArrayList<>();
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

    @Override
    public void syncElasticSearchWithDB() {
        PutMethod method = new PutMethod("http://localhost:9200/_river/"+config.getEsConfig().getRiver()+"/_meta");

        try {
            DbConfig dbConfig = new DbConfig();
            dbConfig.type = "jdbc";
            dbConfig.jdbc.user=config.getEsConfig().getDbUsername();
            dbConfig.jdbc.password=config.getEsConfig().getDbPassword();
            dbConfig.jdbc.url=config.getEsConfig().getDbJdbcUrl();

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
            dbConfig.jdbc.index=config.getEsConfig().getIndex();
            dbConfig.jdbc.type=config.getEsConfig().getType();
            dbConfig.jdbc.interval=30;

            String value = jacksonObjectMapper.writeValueAsString(dbConfig);

            LOGGER.info("DB config for ES sync: [{}]", value);

            method.setRequestEntity(new StringRequestEntity(value, null, "UTF-8"));
            int responseCode = httpManager.executeMethod(method);
            String body = method.getResponseBodyAsString();

            LOGGER.info("Response code for ES sync: [{}]", responseCode);
            LOGGER.info("Response body for ES sync: [{}]", body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            method.releaseConnection();
        }
    }
}
