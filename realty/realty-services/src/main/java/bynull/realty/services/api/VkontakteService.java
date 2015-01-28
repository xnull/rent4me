package bynull.realty.services.api;

import bynull.realty.dto.vk.VkontaktePageDTO;

import java.util.List;

/**
 * Created by dionis on 28/01/15.
 */
public interface VkontakteService {
    List<VkontaktePageDTO> listAllPages();

    void delete(long vkPageId);

    void save(VkontaktePageDTO vkontaktePageDTO);

    VkontaktePageDTO findPageById(long fbPageId);
}
