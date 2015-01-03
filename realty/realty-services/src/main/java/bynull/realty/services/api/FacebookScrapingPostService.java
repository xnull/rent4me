package bynull.realty.services.api;

import bynull.realty.components.FacebookHelperComponent;

import java.util.List;

/**
 * Created by dionis on 02/01/15.
 */
public interface FacebookScrapingPostService {
    void scrapNewPosts();

    void syncElasticSearchWithDB();
}
