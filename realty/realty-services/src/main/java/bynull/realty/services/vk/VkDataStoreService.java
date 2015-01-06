package bynull.realty.services.vk;


import bynull.realty.data.business.vk.Item;

import java.util.List;

/**
 * Created by trierra on 12/17/14.
 */
public interface VkDataStoreService {

    void savePost(Item post);

    void savePosts(List<Item> post);

    Item getPostById(Long id);

    List<Item> getPostsList();

    void removePost();
}
