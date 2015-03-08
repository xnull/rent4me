package bynull.realty.dao;

import bynull.realty.common.Porter;
import bynull.realty.data.business.Apartment;
import bynull.realty.util.LimitAndOffset;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom, InitializingBean {
    @PersistenceContext
    EntityManager entityManager;

    Porter porter;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
    }

    @Override
    public List<Apartment> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, ApartmentRepository.FindMode findMode) {
        Assert.notNull(text);
        Assert.notNull(roomsCount);
        Assert.notNull(findMode);

        Assert.notNull(roomsCount);
        text = StringUtils.trimToEmpty(text);
        final String targetJPQL;

        String searchText = text.isEmpty() ? "" : text.length() > 5 ? porter.stem(text) : text;

        String qlString = "select p from Apartment p where p.published=true AND p.target IN (:targets) " +
                (!searchText.isEmpty() ? " AND lower(p.description) like :msg " : "") +
//                (withSubway ? " AND p.metros IS NOT EMPTY " : "") +
                (!roomsCount.isEmpty() ? " AND p.roomCount IN (:roomCounts) " : "") +
                (minPrice != null ? " AND p.rentalFee >= :minPrice " : "") +
                (maxPrice != null ? " AND p.rentalFee <= :maxPrice " : "") +
                " ORDER BY p.logicalCreated DESC";
        TypedQuery<Apartment> query = entityManager.createQuery(qlString, Apartment.class);
        Apartment.Target value = findMode.toTarget();
        query.setParameter("targets", ImmutableList.of(value, Apartment.Target.BOTH));

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

    private static class Pair {
        private final String paramName;
        private final String value;

        public Pair(String paramName, String value) {
            this.paramName = paramName;
            this.value = value;
        }
    }

    @Override
    public Set<String> similarApartments(Set<String> texts) {
        Assert.notNull(texts);
        if(texts.isEmpty()) return Collections.emptySet();

        AtomicInteger i = new AtomicInteger();
        List<Pair> expr = texts.stream().map(t->new Pair("p_"+i.incrementAndGet(), t)).collect(Collectors.toList());

        String sqlPart = expr.stream().map(p->" lower(a.description) like :"+p.paramName).collect(Collectors.joining(" OR "));

        Query query = entityManager.createNativeQuery("select a.description from apartments a where " + sqlPart);

        expr.forEach(p -> {
            query.setParameter(p.paramName, p.value);
        });


        Set<String> result = (Set<String>) query.getResultList().stream().collect(Collectors.toSet());
        return result;
    }
}
