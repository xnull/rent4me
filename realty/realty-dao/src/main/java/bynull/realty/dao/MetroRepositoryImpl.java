package bynull.realty.dao;

import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.GeoPoint;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by dionis on 3/13/15.
 */
public class MetroRepositoryImpl implements MetroRepositoryCustom {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<MetroEntity> findMetros(ApartmentRepositoryCustom.GeoParams geoParams) {

        String qlString = "select m.* from metro_stations m where "
                ;

        Map<String, Object> params = new HashMap<>();

        final String ordering;

        boolean andNeeded = false;

        if(geoParams.getCountryCode().isPresent()) {

            /*qlString += " upper(m.country_code)=upper(:country_code) ";
            params.put("country_code", geoParams.getCountryCode().get());
            andNeeded = true;*/
        }

        if (geoParams.getBoundingBox().isPresent()) {
            if(andNeeded) {
                qlString += " AND ";
            }

            qlString += " st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_low," +
                    "' '," +
                    ":lat_low," +
                    "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_high," +
                    "' '," +
                    ":lat_high," +
                    "')'))), 4326)" +
                    " ~ m.location" +
                    " ";

            ApartmentRepositoryCustom.BoundingBox boundingBox = geoParams.getBoundingBox().get();
            params.put("lng_low", boundingBox.getLow().getLongitude());
            params.put("lat_low", boundingBox.getLow().getLatitude());
            params.put("lng_high", boundingBox.getHigh().getLongitude());
            params.put("lat_high", boundingBox.getHigh().getLatitude());

            andNeeded = true;
        }


        Optional<GeoPoint> point = geoParams.getPoint();
        if(point.isPresent()) {

            String createGeoPoint = "ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') )";

            if(andNeeded) {
                qlString += " AND ";
            }
            //5000 meters
            qlString += " ST_Distance_Sphere(m.location, " + createGeoPoint + " ) <= 5000";

//            ordering = " order by m.location <-> "+createGeoPoint+", m.name";
            ordering = " order by m.name";
            params.put("lat", (point).get().getLatitude());
            params.put("lng", (point).get().getLongitude());

            andNeeded = true;
        } else {
            ordering = " ORDER BY m.name";
        }


        qlString += ordering;

        Query query = em.createNativeQuery(qlString, MetroEntity.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        List<MetroEntity> resultList = query.getResultList();
        return resultList;
    }
}
