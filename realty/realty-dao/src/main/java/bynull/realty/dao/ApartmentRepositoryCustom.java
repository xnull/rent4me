package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import bynull.realty.util.LimitAndOffset;

import java.util.List;
import java.util.Set;

/**
 * Created by dionis on 3/5/15.
 */
public interface ApartmentRepositoryCustom {
    List<Apartment> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, ApartmentRepository.FindMode findMode);

    Set<String> similarApartments(Set<String> hashes);
}
