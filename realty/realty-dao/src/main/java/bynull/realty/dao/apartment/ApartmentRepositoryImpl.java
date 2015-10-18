package bynull.realty.dao.apartment;

import bynull.realty.common.Porter;
import bynull.realty.dao.JooqUtil;
import bynull.realty.dao.apartment.ApartmentRepository.FindMode;
import bynull.realty.dao.apartment.ApartmentRepository.RoomCount;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.Apartment.Target;
import bynull.realty.data.business.apartment.ApartmentIdent;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.HibernateUtil;
import com.google.common.collect.ImmutableList;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
@Slf4j
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom, InitializingBean {
    @PersistenceContext
    EntityManager entityManager;

    @Resource
    CityRepository cityRepository;

    @Resource
    private ApartmentIdentRepository apartmentIdentRepo;

    Porter porter;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
    }

    @Override
    public List<Apartment> findSimilarApartments(long apartmentId) {
        Apartment apartment = entityManager.find(Apartment.class, apartmentId);
        if (apartment == null) {
            throw new EntityNotFoundException("Apartment with id " + apartmentId + " not found");
        }
        apartment = HibernateUtil.deproxy(apartment);
        FindSimilarApartmentsSqlBuilder queryBuilder = new FindSimilarApartmentsSqlBuilder(apartment, apartmentId);

        String sql = queryBuilder.findSimilarApartmentsQuery();
        Query query = entityManager.createNativeQuery(sql, Apartment.class).setMaxResults(10);

        for (Map.Entry<String, Object> entry : queryBuilder.findSimilarApartmentsMap().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        List<Apartment> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public List<Apartment> findPosts(FindPostsParameters params) {
        log.trace("Find posts: {}", params);

        Optional<GeoPoint> point = params.geoParams.getPoint();
        final Optional<BoundingBox> boundingBox;
        if (params.geoParams.getBoundingBox().isPresent()) {
            boundingBox = params.geoParams.getBoundingBox();
        } else {
            CityEntity city = cityRepository.findByPoint(point.get().getLongitude(), point.get().getLatitude());
            boundingBox = Optional.ofNullable(city != null ? city.getArea() : null);
        }

        ApartmentFindPostsSql sqlBuilder = new ApartmentFindPostsSql(params, boundingBox);

        Query query = entityManager.createNativeQuery(sqlBuilder.findPosts(), Apartment.class);

        for (Map.Entry<String, Object> entry : sqlBuilder.findPostsParamsMap().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        List<Apartment> resultList = query
                .setFirstResult(params.limitAndOffset.offset)
                .setMaxResults(params.limitAndOffset.limit)
                .getResultList();

        return resultList;
    }

    @Override
    public List<ApartmentIdent> getApartmentIdents(Long apartmentId) {
        log.trace("Get apartment idents: {}", apartmentId);
        TypedQuery<ApartmentIdent> query = entityManager
                .createQuery("select from ApartmentIdent aId where aId.apartmentId = :aptId", ApartmentIdent.class);

        return query.getResultList();
    }

    @Override
    public void saveApartmentIdents(Set<Long> idents, Long apartmentId) {
        log.debug("Save apartment idents: {}", apartmentId);

        for (Long identId : idents) {
            ApartmentIdent aptIdent = apartmentIdentRepo.findByApartmentIdAndIdentId(apartmentId, identId);

            if (aptIdent == null) {
                ApartmentIdent ident = new ApartmentIdent();
                ident.setApartmentId(apartmentId);
                ident.setIdentId(identId);
                apartmentIdentRepo.saveAndFlush(ident);
            }
        }
    }

    @Override
    public Set<String> similarApartments(Set<String> hashes) {
        Assert.notNull(hashes);
        if (hashes.isEmpty()) {
            return Collections.emptySet();
        }

        DefaultDSLContext ctx = JooqUtil.getContext();
        SelectJoinStep<?> sql = ctx.select(DSL.field("a.description")).from(DSL.table("apartments").as("a"));
        sql.where("a.description_hash IN (:hashez)");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("hashez", hashes);

        return (Set<String>) query.getResultList().stream().collect(Collectors.toSet());
    }

    public static String blackApartmentsQuery(){
        DSLContext ctx = JooqUtil.getContext();
        SelectJoinStep<?> query = ctx.select(DSL.field("aptident.apartment_id"))
                .from(DSL.table(IdentEntity.T_IDENT).as("ident"))
                .join("blacklist black").on("ident.id = black.ident_id")
                .join("apartment_ident aptident").on("ident.id = aptident.ident_id");
        return query.toString();
    }
}
