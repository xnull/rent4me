package bynull.realty.services.vk.impl;

import bynull.realty.dao.vk.VkRepository;
import bynull.realty.data.business.vk.Item;
import bynull.realty.services.vk.VkDataStoreService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by trierra on 12/17/14.
 */
@Component
public class VkDataStoreImpl implements VkDataStoreService {

    @Resource
    private VkRepository vkRepository;

    @Override
    public void savePost(Item post) {
        vkRepository.save(post);
    }

    @Override
    public void savePosts(List<Item> post) {
        vkRepository.save(post);
    }

    @Override
    public Item getPostById(Long id) {
        return null;
    }

    @Override
    public List<Item> getPostsList() {
        return null;
    }

    @Override
    public void removePost() {

    }
}
