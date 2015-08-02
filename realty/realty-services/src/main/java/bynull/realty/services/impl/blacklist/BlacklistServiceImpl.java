package bynull.realty.services.impl.blacklist;

import bynull.realty.dao.UserRepository;
import bynull.realty.dao.blacklist.BlacklistRepository;
import bynull.realty.data.business.blacklist.BlacklistEntity;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ContactDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.impl.IdentificationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private IdentificationServiceImpl identService;

    @Resource
    private ApartmentService apartmentService;

    /**
     * 1. Найти в блэк листе запись о данном ресурсе
     * 2. Если её нет, то создать
     *
     * @param apartmentId
     */
    public void addApartmentToBlacklist(Long apartmentId) {
        log.debug("Vote to add advert to blacklist. Id: {}", apartmentId);

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

        Optional<ApartmentDTO> optApartment = apartmentService.find(apartmentId);
        if (!optApartment.isPresent()){
            return;
        }

        IdentEntity ident = identService.find(apartmentId.toString(), IdentType.APARTMENT);
        if (ident == null) {
            ident = identService.save(apartmentId.toString(), IdentType.APARTMENT);
        }

        Optional<BlacklistEntity> optBlackInfo = Optional.ofNullable(blacklistRepo.findByIdentId(ident.getId()));

        if (!optBlackInfo.isPresent()) {
            BlacklistEntity bl = new BlacklistEntity();
            bl.setIdentId(ident.getId());
            blacklistRepo.save(bl);
        }

        mergeIdents(ident, optApartment);
    }

    /**
     * Связать имеющиеся идентификаторы
     * @param sourceIdent
     * @param optApartment
     */
    private void mergeIdents(IdentEntity sourceIdent, Optional<ApartmentDTO> optApartment) {

        /**
         * 1. Находим все идентификаторы с которыми связаны данные апартаменты
         * 2. связываем все иденты
         * 3. добавляем ссылку на ids в черный список
         */
        if (optApartment.isPresent()) {
            ApartmentDTO apartment = optApartment.get();
            switch (apartment.getDataSource()) {
                case INTERNAL:
                    identService.mergeIdents(
                            sourceIdent,
                            getInternalApartmentIdents(apartment.getOwner())
                    );
                    break;
                case FACEBOOK:
                    identService.mergeIdents(
                            sourceIdent,
                            getSocialNetIdents(apartment, IdentType.FB_ID)
                    );
                    break;
                case VKONTAKTE:
                    identService.mergeIdents(
                            sourceIdent,
                            getSocialNetIdents(apartment, IdentType.VK_ID)
                    );
                    break;
            }

        } else {
            /**
             * вываливаем в лог ошибку, о том что чел хочет странного и такого объявления не существует в базе,
             */
        }
    }

    private Set<Long> getSocialNetIdents(ApartmentDTO apartment, IdentType type) {
        Set<Long> adjIdents = new HashSet<>();
        adjIdents.add(identService.find(apartment.getAuthorId(), type).getId());

        for (ContactDTO contact : apartment.getContacts()) {
            switch (contact.getType()) {
                case PHONE:
                    adjIdents.add(identService.find(contact.getPhoneNumber().getRawNumber(), IdentType.PHONE).getId());
                    break;
            }
        }

        return adjIdents;
    }

    private Set<Long> getInternalApartmentIdents(UserDTO user) {
        if (user == null) return Collections.emptySet();

        Set<Long> result = new HashSet<>();
        result.add(identService.find(user.getId().toString(), IdentType.USER_ID).getId());
        result.add(identService.find(user.getEmail(), IdentType.EMAIL).getId());
        result.add(identService.find(user.getFacebookId(), IdentType.FB_ID).getId());
        result.add(identService.find(user.getVkontakteId(), IdentType.VK_ID).getId());
        result.add(identService.find(user.getPhoneNumber(), IdentType.PHONE).getId());

        return result;
    }
}
