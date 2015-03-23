package bynull.realty.dao;

import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.BoundingBox;
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

        String createGeoPoint = "ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') )";

        String qlString = "select m.* from metro_stations m where m.city_id IN (select c.id from cities c where c.area ~ "+createGeoPoint+") ";

        Map<String, Object> params = new HashMap<>();

        final String ordering = " ORDER BY m.name";
        boolean andNeeded = false;

        if(geoParams.getCountryCode().isPresent()) {

            /*qlString += " upper(m.country_code)=upper(:country_code) ";
            params.put("country_code", geoParams.getCountryCode().get());
            andNeeded = true;*/
        }



        final GeoPoint centerPoint;
        if (geoParams.getBoundingBox().isPresent()) {
            if(andNeeded) {
                qlString += " AND ";
            }
/*
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
*/
            BoundingBox boundingBox = geoParams.getBoundingBox().get();
//            params.put("lng_low", boundingBox.getLow().getLongitude());
//            params.put("lat_low", boundingBox.getLow().getLatitude());
//            params.put("lng_high", boundingBox.getHigh().getLongitude());
//            params.put("lat_high", boundingBox.getHigh().getLatitude());


            //calculate center point

            double averageLatitude = (boundingBox.getLow().getLatitude() + boundingBox.getHigh().getLatitude()) / 2.0d;
            double averageLongitude = (boundingBox.getLow().getLongitude() + boundingBox.getHigh().getLongitude()) / 2.0d;

            centerPoint = new GeoPoint().withLatitude(averageLatitude).withLongitude(averageLongitude);

            andNeeded = true;
        } else {


            Optional<GeoPoint> point = geoParams.getPoint();
            if (point.isPresent()) {


                if (andNeeded) {
                    qlString += " AND ";
                }
                //5000 meters
//                qlString += " ST_Distance_Sphere(m.location, " + createGeoPoint + " ) <= 5000";

//            ordering = " order by m.location <-> "+createGeoPoint+", m.name";
//                ordering = " order by m.name";
//                params.put("lat", (point).get().getLatitude());
//                params.put("lng", (point).get().getLongitude());

                centerPoint = point.get();

                andNeeded = true;
            } else {
                throw new IllegalArgumentException("Boundning area either geopoint needed");
            }

        }
        qlString += ordering;

        params.put("lat", centerPoint.getLatitude());
        params.put("lng", centerPoint.getLongitude());

        Query query = em.createNativeQuery(qlString, MetroEntity.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        List<MetroEntity> resultList = query.getResultList();
        return resultList;
    }
}
