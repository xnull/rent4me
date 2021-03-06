package bynull.realty.services.api;

import bynull.realty.dao.apartment.ApartmentRepository;
import bynull.realty.dto.SocialNetPostDTO;
import bynull.realty.util.LimitAndOffset;

import java.util.List;
import java.util.Set;

/**
 * Created by dionis on 04/02/15.
 */
public interface SocialNetService {
    List<? extends SocialNetPostDTO> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, ApartmentRepository.FindMode findMode);
}
