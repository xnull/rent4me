package bynull.realty.crawler.api;

import bynull.realty.crawler.json.BaseEntity;

import java.util.List;

/**
 * Created by trierra on 12/17/14.
 */
public interface VkDataStore {
    void savePost(List<BaseEntity> postsList);
}
