package bynull.realty.services.vk.impl;

import bynull.realty.dao.vk.VkAttachmentRepository;
import bynull.realty.dao.vk.VkItemRepository;
import bynull.realty.data.business.vk.Item;
import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.services.vk.VkDataStoreService;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trierra on 12/17/14.
 */
@Service
public class VkDataStoreServiceImpl implements VkDataStoreService {

    @Resource
    private VkItemRepository vkItemRepository;

    @Resource
    private VkAttachmentRepository vkAttachmentRepository;

    public VkDataStoreServiceImpl() {
    }

    @Override
    public void savePost(ItemDTO post) {
        vkItemRepository.save(post.toInternal());
    }

    @Override
    public void savePosts(List<ItemDTO> post) {
        List<Item> dtoList = post.stream().map(ItemDTO::toInternal).collect(Collectors.toList());
        for (Item item : dtoList) {
            vkAttachmentRepository.save(item.getAttachments());
            vkItemRepository.save(item);
        }
    }

    @Override
    public ItemDTO getPostById(Long id) {
        throw new NotImplementedException();
    }

    @Override
    public List<ItemDTO> getPostsList() {
        throw new NotImplementedException();
    }

    @Override
    public void removePost() {
        throw new NotImplementedException();
    }
}
