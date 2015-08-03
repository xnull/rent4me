package bynull.realty.services.api;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 28/01/15.
 */
public interface VkontakteService {
    void syncWithVK();

    List<? extends VkontaktePageDTO> listAllPages();

    void delete(long vkPageId);

    void save(VkontaktePageDTO vkontaktePageDTO);

    Optional<VkontaktePageDTO> findPageById(long vkPageId);

    long countByQuery(String text);

    List<ApartmentDTO> findPosts(String text, PageRequest created);

    void reparseExistingVKPosts();
}
