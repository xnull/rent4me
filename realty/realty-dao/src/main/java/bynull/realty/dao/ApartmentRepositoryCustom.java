package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.util.LimitAndOffset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by dionis on 3/5/15.
 */
public interface ApartmentRepositoryCustom {
    List<Apartment> findPosts(String text,
                              boolean withSubway,
                              Set<ApartmentRepository.RoomCount> roomsCount,
                              Integer minPrice,
                              Integer maxPrice,
                              ApartmentRepository.FindMode findMode, GeoParams geoParams, List<Long> metroIds, LimitAndOffset limitAndOffset);


    @Wither
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class GeoParams {
        private Optional<String> countryCode;

        private Optional<BoundingBox> boundingBox;
        private Optional<GeoPoint> point;
    }

    Set<String> similarApartments(Set<String> hashes);
}
