package bynull.realty.services.api;

import bynull.realty.dto.FacebookPostDTO;
import bynull.realty.util.LimitAndOffset;

import java.util.List;

/**
 * Created by dionis on 02/01/15.
 */
public interface FacebookScrapingPostService {
    void scrapNewPosts();

    void syncElasticSearchWithDB();

    List<FacebookPostDTO> findRenterPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);

    List<FacebookPostDTO> findLessorPosts(String text, boolean withSubway, LimitAndOffset limitAndOffset);
}
