package bynull.realty.services.api;

import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.util.LimitAndOffset;

import java.util.List;

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

    FacebookPageDTO findPageById(long fbPageId);
}
