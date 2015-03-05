package bynull.realty.services.api;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Created by dionis on 28/01/15.
 */
public interface VkontakteService {
    void syncWithVK();

    List<VkontaktePageDTO> listAllPages();

    void delete(long vkPageId);

    void save(VkontaktePageDTO vkontaktePageDTO);

    VkontaktePageDTO findPageById(long fbPageId);

    long countByQuery(String text);

    List<ApartmentDTO> findPosts(String text, PageRequest created);

    void reparseExistingVKPosts();
}
