package bynull.realty.dao;

import bynull.realty.common.Porter;
import bynull.realty.data.business.Apartment;
import bynull.realty.util.LimitAndOffset;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom, InitializingBean {
    @PersistenceContext
    EntityManager entityManager;

    @PersistenceContext
    EntityManager em;

    Porter porter;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
    }

    @Override
    public List<Apartment> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, ApartmentRepository.FindMode findMode) {
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
                .map(word -> " ( lower(p.description) like '" + ilike(word) + "' ) ")
                .collect(Collectors.joining(" OR "));

        String searchText = text.isEmpty() ? "" : text.length() > 5 ? porter.stem(text) : text;

        String qlString = "select p from Apartment p where p.published=true AND (" + findModeJPQL + ")" +
                (!searchText.isEmpty() ? " AND lower(p.description) like :msg " : "") +
//                (withSubway ? " AND p.metros IS NOT EMPTY " : "") +
                (!roomsCount.isEmpty() ? " AND p.roomCount IN (:roomCounts) " : "") +
                (minPrice != null ? " AND p.rentalFee >= :minPrice " : "") +
                (maxPrice != null ? " AND p.rentalFee <= :maxPrice " : "") +
                " ORDER BY p.logicalCreated DESC";
        TypedQuery<Apartment> query = em.createQuery(qlString, Apartment.class);

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

        List<Apartment> resultList = query
                .setFirstResult(limitAndOffset.offset)
                .setMaxResults(limitAndOffset.limit)
                .getResultList();

        return resultList;
    }

    private String ilike(String text) {
        return !text.isEmpty() ? "%" + text.toLowerCase() + "%" : "%";
    }
}
