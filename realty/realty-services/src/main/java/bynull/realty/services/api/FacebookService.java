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
    void syncWithFB();

    void syncElasticSearchWithDB();

    void save(FacebookPageDTO pageDTO);

    void delete(long id);

    List<FacebookPageDTO> listAllPages();

    /**
     * @deprecated Use {@link bynull.realty.services.api.SocialNetService#findPosts(String, boolean, java.util.Set, Integer, Integer, bynull.realty.util.LimitAndOffset, FindMode) instead}
     * @param text
     * @param withSubway
     * @param limitAndOffset
     * @return
     */
    @Deprecated
    List<FacebookPostDTO> findRenterPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);

    /**
     * @deprecated Use {@link bynull.realty.services.api.SocialNetService#findPosts(String, boolean, java.util.Set, Integer, Integer, bynull.realty.util.LimitAndOffset, FindMode) instead}
     * @param text
     * @param withSubway
     * @param limitAndOffset
     * @return
     */
    @Deprecated
    List<FacebookPostDTO> findLessorPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);

    /**
     * @deprecated Use {@link bynull.realty.services.api.SocialNetService#findPosts(String, boolean, java.util.Set, Integer, Integer, bynull.realty.util.LimitAndOffset, FindMode) instead}
     * @param text
     * @param withSubway
     * @param roomsCount
     * @param minPrice
     * @param maxPrice
     * @param limitAndOffset
     * @param findMode
     * @return
     */
    @Deprecated
    List<FacebookPostDTO> findFBPosts(String text, boolean withSubway, Set<RoomCount> roomsCount, Integer minPrice, Integer maxPrice, LimitAndOffset limitAndOffset, FindMode findMode);

    FacebookPageDTO findPageById(long fbPageId);

    List<FacebookPostDTO> findPosts(String text, PageRequest pageRequest);

    long countOfPages();

    void reparseExistingFBPosts();

    long countByQuery(String text);

}
