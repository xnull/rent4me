package bynull.realty.services.impl.socialnet.vk;

import bynull.realty.converters.VkontaktePageModelDTOConverter;
import bynull.realty.dao.external.VkontaktePageRepository;
import bynull.realty.data.business.external.vkontakte.AVkontaktePage;
import bynull.realty.dto.vk.VkontaktePageDTO;
import bynull.realty.services.api.VkontakteService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dionis on 28/01/15.
 */
@Service
public class VkontakteServiceImpl implements VkontakteService {

    @Resource
    VkontaktePageRepository vkontaktePageRepository;

    @Resource
    VkontaktePageModelDTOConverter vkontaktePageConverter;

    @Transactional(readOnly = true)
    @Override
    public List<VkontaktePageDTO> listAllPages() {
        return vkontaktePageConverter.toTargetList(vkontaktePageRepository.findAll(new Sort(Sort.Direction.DESC, "updated")));
    }

    @Transactional
    @Override
    public void delete(long vkPageId) {
        vkontaktePageRepository.delete(vkPageId);
    }

    @Transactional
    @Override
    public void save(VkontaktePageDTO vkontaktePageDTO) {
        AVkontaktePage one = vkontaktePageDTO.getId() != null ? vkontaktePageRepository.findOne(vkontaktePageDTO.getId()) : new AVkontaktePage();

        one.setLink(vkontaktePageDTO.getLink());
        one.setExternalId(vkontaktePageDTO.getExternalId());
        vkontaktePageRepository.saveAndFlush(one);
    }

    @Transactional(readOnly = true)
    @Override
    public VkontaktePageDTO findPageById(long fbPageId) {
        return vkontaktePageConverter.toTargetType(vkontaktePageRepository.findOne(fbPageId));
    }
}
