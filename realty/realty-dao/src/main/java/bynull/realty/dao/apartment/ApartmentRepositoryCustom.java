package bynull.realty.dao.apartment;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.apartment.ApartmentIdent;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.util.LimitAndOffset;
import lombok.*;
import lombok.experimental.Wither;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by dionis on 3/5/15.
 */
public interface ApartmentRepositoryCustom {
    List<Apartment> findSimilarApartments(long apartmentId);

    List<Apartment> findPosts(FindPostsParameters queryParams);

    List<ApartmentIdent> getApartmentIdents(Long apartmentId);
    void saveApartmentIdents(Set<Long> idents, Long apartmentId);

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

    @ToString
    @Value
    class FindPostsParameters {
        public final String text;
        public final boolean withSubway;
        public final Set<ApartmentRepository.RoomCount> roomsCount;
        public final Integer minPrice;
        public final Integer maxPrice;
        public final ApartmentRepository.FindMode findMode;
        public final GeoParams geoParams;
        public final List<Long> metroIds;
        public final LimitAndOffset limitAndOffset;
    }
}
