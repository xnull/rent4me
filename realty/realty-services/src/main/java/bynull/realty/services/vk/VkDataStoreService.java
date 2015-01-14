package bynull.realty.services.vk;


import bynull.realty.dto.vk.ItemDTO;

import java.util.List;

/**
 * Created by trierra on 12/17/14.
 */
public interface VkDataStoreService {

    void savePost(ItemDTO post);

    void savePosts(List<ItemDTO> post);

    ItemDTO getPostById(Long id);

    List<ItemDTO> getPostsList();

    void removePost();
}
