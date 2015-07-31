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

        /**
         * Для identifications таблицы надо будет реализовать алгоритм связывания идентификаторов.
         * Например, мы создали 2 записи в ids таблице. В первой записи лежит телефон во второй почта.
         * Через некоторое время мы обнаруживаем, что телефон и почта принадлежат одному и тому-же лицу.
         * Вопрос: что делать?
         * Варианты:
         *  1. Объединить записи, удалив одну из них или создав 3-ю запись, удалив 2 предыдущие. Путь опасен
         *  2. Создаем таблицу ids_ids отношение многие ко многим, в таблице 2 столбца каждый указывает на ids. Когда
         *     связь между идентификаторами будет обнаружена мы добавим запись в ids_ids. И по этой таблице сможем
         *     находить все связи всех идентификаторов.
         */

        if (!optBlackInfo.isPresent()) {
            Optional<ApartmentDTO> optApartment = apartmentService.find(apartmentId);

            /**
             * 1. Находим все идентификаторы с которыми связаны данные апартаменты
             * 2. Ищем все идентификаторы с ids_ids и ids таблице и если обнаружились новые связи идентификаторов
             *    через объявление, то связываем все имеющиеся идентификаторы путем обновления таблиц ids_ids и ids
             * 3. добавляем ссылку на ids в черный список
             */
            if (optApartment.isPresent()){
                Blacklist bl = new Blacklist();
                bl.setApartmentId(optApartment.get().getId());

                switch (optApartment.get().getDataSource()) {
                    case INTERNAL:
                        //check that identification exists if not then create it
                    /*idRepo.findByUserId();
                    idRepo.findByVkId();
                    idRepo.findByFbId();
                    idRepo.findByEmail();
                    idRepo.findByPhone();

                    //если нашли ид хоть по одному из параметров, то добавляем его в черный список
                    if(found){
                        bl.setIdentificationId(optApartment.getOwner().getId());
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

            }else {
                /**
                 * вываливаем в лог ошыбку, о том что произошла неведомая херня
                 */
            }

            //blacklistRepo.save()
        } else {
            //алгоритм тот же что и выше, только надо создать запись в блэк таблице
        }
    }
}
