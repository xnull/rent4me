package bynull.realty.services.impl.socialnet;

import bynull.realty.components.text.Porter;
import bynull.realty.converters.SocialNetPostModelDTOConverter;
import bynull.realty.data.business.external.SocialNetPost;
import bynull.realty.dto.SocialNetPostDTO;
import bynull.realty.services.api.FindMode;
import bynull.realty.services.api.RoomCount;
import bynull.realty.services.api.SocialNetService;
import bynull.realty.util.LimitAndOffset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 04/02/15.
 */
@Service
public class SocialNetServiceImpl implements SocialNetService, InitializingBean {

    @Resource
    SocialNetPostModelDTOConverter socialNetPostConverter;

    @PersistenceContext
    EntityManager em;

    Porter porter;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SocialNetPostDTO> findPosts(String text, boolean withSubway, Set<RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, FindMode findMode) {
        Assert.notNull(text);
        Assert.notNull(roomsCount);

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

        String qlString = "select p from SocialNetPost p where (" + findModeJPQL + ")" +
                (!searchText.isEmpty() ? " AND lower(p.message) like :msg " : "") +
//                (withSubway ? " AND p.metros IS NOT EMPTY " : "") +
                (!roomsCount.isEmpty() ? " AND p.roomCount IN (:roomCounts) " : "") +
                (minPrice != null ? " AND p.rentalFee >= :minPrice " : "") +
                (maxPrice != null ? " AND p.rentalFee <= :maxPrice " : "") +
                " ORDER BY p.created DESC";
        TypedQuery<SocialNetPost> query = em.createQuery(qlString, SocialNetPost.class);

        if (!searchText.isEmpty()) {
            query.setParameter("msg", ilike(searchText));
        }

        if (!roomsCount.isEmpty()) {
            Set<Integer> values = roomsCount.stream().map(rc -> Integer.valueOf(rc.value)).collect(Collectors.toSet());
            query.setParameter("roomCounts", values);
        }

        if (minPrice != null) {
            query.setParameter("minPrice", BigDecimal.valueOf(minPrice));
        }

        if (maxPrice != null) {
            query.setParameter("maxPrice", BigDecimal.valueOf(maxPrice));
        }

        List<SocialNetPost> resultList = query
                .setFirstResult(limitAndOffset.offset)
                .setMaxResults(limitAndOffset.limit)
                .getResultList();

        return socialNetPostConverter.toTargetList(resultList);
    }

    private String ilike(String text) {
        return !text.isEmpty() ? "%" + text.toLowerCase() + "%" : "%";
    }
}
