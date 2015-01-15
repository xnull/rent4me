package bynull.realty.services.vk.impl;

import bynull.realty.dao.vk.VkRepository;
import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.services.vk.VkDataStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trierra on 12/17/14.
 */
@Service
public class VkDataStoreServiceImpl implements VkDataStoreService {

    @Resource
    private VkRepository vkRepository;

    public VkDataStoreServiceImpl() {
    }

    @Override
    public void savePost(ItemDTO post) {

        vkRepository.save(post.toInternal());
    }

    @Override
    public void savePosts(List<ItemDTO> post) {
        vkRepository.save(post.stream().map(ItemDTO::toInternal).collect(Collectors.toList()));
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
