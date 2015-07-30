package bynull.realty.services.impl.blacklist;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.IdentificationRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.dao.blacklist.BlacklistRepository;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.blacklist.Blacklist;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Сервис для работы с черным спиком. Идея такая: у нас имеется таблица blacklist со списком заблокированнных ресурсов.
 * Эта таблица ссылается на таблицу identification которая нужна для хранения .....
 * Created by null on 7/26/15.
 */
@Service
@Slf4j
public class BlacklistServiceImpl {

    @Resource
    private BlacklistRepository blacklistRepo;

    @Resource
    private IdentificationRepository idRepo;

    @Resource
    private ApartmentService apartmentService;

    @Resource
    private UserRepository userRepo;

    /**
     * 1. Найти в блэк листе запись о данном ресурсе
     * 2. Если её нет, то создать
     *
     * @param apartmentId
     */
    public void voteToAddAdvertToBlacklist(Long apartmentId) {
        log.debug("Vote to add advert to blacklist. Id: {}", apartmentId);

        Optional<Blacklist> optBlackInfo = Optional.ofNullable(blacklistRepo.findByApartmentId(apartmentId));

        if (!optBlackInfo.isPresent()) {
            Optional<ApartmentDTO> apartment = apartmentService.find(apartmentId);

            Blacklist bl = new Blacklist();
            bl.setApartmentId(apartment.get().getId());
            switch (apartment.get().getDataSource()) {
                case INTERNAL:
                    //check that identification exists if not then create it
                    /*idRepo.findByUserId();
                    idRepo.findByVkId();
                    idRepo.findByFbId();
                    idRepo.findByEmail();
                    idRepo.findByPhone();

                    //если нашли ид хоть по одному из параметров, то добавляем его в черный список
                    if(found){
                        bl.setIdentificationId(apartment.getOwner().getId());
                        ...
                    }else{
                        создать новый ид и вписать туда имеющиеся знания о нарушителе
                        idRepo.save(id);
                    }*/
                    break;
                case FACEBOOK:
                    //get fb user id
                    break;
                case VKONTAKTE:
                    //get vk user
                    break;
            }


            //blacklistRepo.save()
        } else {

        }
    }
}
