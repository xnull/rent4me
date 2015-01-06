package bynull.realty.grabber.services.impl;

import bynull.realty.dao.vk.VkRepository;
import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.grabber.services.api.VkDataStoreService;
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
    public void savePost(ItemDTO post) {
        vkRepository.save(post);
    }

    @Override
    public void savePosts(List<ItemDTO> post) {
        vkRepository.save(post);
    }

    @Override
    public ItemDTO getPostById(Long id) {
        return null;
    }

    @Override
    public List<ItemDTO> getPostsList() {
        return null;
    }

    @Override
    public void removePost() {

    }
}
