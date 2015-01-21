package bynull.realty.services.api;

import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.util.LimitAndOffset;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by dionis on 02/01/15.
 */
public interface FacebookService {
    void scrapNewPosts();

    void syncElasticSearchWithDB();

    void save(FacebookPageDTO pageDTO);

    void delete(long id);

    List<FacebookPageDTO> listAllPages();

    List<FacebookPostDTO> findRenterPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);

    List<FacebookPostDTO> findLessorPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);

    List<FacebookPostDTO> findFBPosts(String text, boolean withSubway, Set<RoomCount> roomsCount, LimitAndOffset limitAndOffset, FindMode findMode);

    FacebookPageDTO findPageById(long fbPageId);

    List<FacebookPostDTO> findPosts(PageRequest pageRequest);

    long countOfPages();

    void reparseExistingFBPosts();

    static enum FindMode {
        RENTER, LESSOR
    }

    static enum RoomCount {
        ONE("1"), TWO("2"), THREE("3"), FOUR_PLUS("4+");

        public final String value;

        RoomCount(String value) {
            this.value = value;
        }

        public static RoomCount findByValueOrFail(String value) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
            for (RoomCount roomCount : values()) {
                if (roomCount.value.equals(value)) return roomCount;
            }
            throw new IllegalArgumentException();
        }
    }
}
