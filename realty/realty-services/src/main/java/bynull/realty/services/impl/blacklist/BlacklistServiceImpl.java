package bynull.realty.services.impl.blacklist;

import bynull.realty.common.PhoneUtil;
import bynull.realty.dao.blacklist.BlacklistRepository;
import bynull.realty.dao.util.IdentRefiner;
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
import java.util.stream.Collectors;

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
    public Set<BlacklistEntity> addApartmentToBlacklist(Long apartmentId) {
        log.debug("Vote to add advert to blacklist. Id: {}", apartmentId);

        /**
         * Для identifications таблицы надо будет реализовать алгоритм связывания идентификаторов.
         * Например, мы создали 2 записи в ids таблице. В первой записи лежит телефон во второй почта.
         * Через некоторое время мы обнаруживаем, что телефон и почта принадлежат одному и тому-же лицу.
         * Вопрос: что делать?
         * Варианты:
         *  1. Объединить записи, удалив одну из них или создав 3-ю запись, удалив 2 предыдущие. Путь опасен
         *  2. Создаем таблицу ids_ids отношение многие ко многим, в таблице 2 столбца каждый указывает на ids. Когда
         *     связь между идентификаторами будет обнаружена мы добавим запись в id_relation. И по этой таблице сможем
         *     находить все связи всех идентификаторов.
         */

        Optional<ApartmentDTO> optApt = apartmentService.find(apartmentId);
        if (!optApt.isPresent()) {
            return Collections.emptySet();
        }

        IdentEntity aptIdent = getIdent(apartmentId);
        saveBl(aptIdent.getId());

        return mergeIdents(aptIdent, optApt.get())
                .stream()
                .map(this::saveBl)
                .collect(Collectors.toSet());
    }

    private BlacklistEntity saveBl(Long ident) {
        BlacklistEntity bl = blacklistRepo.findByIdentId(ident);

        if (bl == null) {
            log.debug("Add new record to bl. Ident id: {}", ident);
            bl = new BlacklistEntity();
            bl.setIdentId(ident);
            return blacklistRepo.save(bl);
        }
        return bl;
    }

    private IdentEntity getIdent(Long apartmentId) {
        return identService.find(apartmentId.toString(), IdentType.APARTMENT)
                .orElseGet(() -> identService.save(apartmentId.toString(), IdentType.APARTMENT));
    }

    /**
     * Связать имеющиеся идентификаторы
     *
     * @param sourceIdent
     * @param apartment
     */
    private Set<Long> mergeIdents(IdentEntity sourceIdent, ApartmentDTO apartment) {
        /**
         * 1. Находим все идентификаторы с которыми связаны данные апартаменты
         * 2. связываем все иденты
         * 3. добавляем ссылку на ids в черный список
         */
        switch (apartment.getDataSource()) {
            case INTERNAL:
                return identService.mergeIdents(sourceIdent, getInternalApartmentIdents(apartment.getOwner()));
            case FACEBOOK:
                return identService.mergeIdents(sourceIdent, getSocialNetIdents(apartment, IdentType.FB_ID));
            case VKONTAKTE:
                return identService.mergeIdents(sourceIdent, getSocialNetIdents(apartment, IdentType.VK_ID));
        }
        return Collections.emptySet();
    }

    private Set<Long> getSocialNetIdents(ApartmentDTO apartment, IdentType type) {
        Set<Long> adjIdents = new HashSet<>();
        adjIdents.add(identService.findAndSaveIfNotExists(apartment.getAuthorId(), type).getId());

        for (ContactDTO contact : apartment.getContacts()) {
            switch (contact.getType()) {
                case PHONE:
                    try {
                        adjIdents.add(identService.findAndSaveIfNotExists(
                                IdentRefiner.refine(contact.getPhoneNumber().getRawNumber(), IdentType.PHONE),
                                IdentType.PHONE).getId()
                        );
                    }
                    catch (Exception e){
                        //ignore
                    }
                    break;
            }
        }

        return adjIdents;
    }

    private Set<Long> getInternalApartmentIdents(UserDTO user) {
        if (user == null) return Collections.emptySet();

        Set<Long> result = new HashSet<>();
        result.add(identService.findAndSaveIfNotExists(user.getId().toString(), IdentType.USER_ID).getId());
        result.add(identService.findAndSaveIfNotExists(user.getEmail(), IdentType.EMAIL).getId());
        result.add(identService.findAndSaveIfNotExists(user.getFacebookId(), IdentType.FB_ID).getId());
        result.add(identService.findAndSaveIfNotExists(user.getVkontakteId(), IdentType.VK_ID).getId());
        result.add(identService.findAndSaveIfNotExists(user.getPhoneNumber(), IdentType.PHONE).getId());

        return result;
    }

    /**
     * Найти если существует соответствующая запись в блек листе.
     * То есть узнать находится ли данный идентификатор в блек листе
     *
     * @param identValue
     * @param identType
     * @return
     */
    public Optional<BlacklistEntity> find(String identValue, IdentType identType) {
        Set<Long> linkedIdentIds = identService.findAllLinkedIdentIds(identValue, identType);
        for (Long linkedIdentId : linkedIdentIds) {
            BlacklistEntity bl = blacklistRepo.findByIdentId(linkedIdentId);
            if (bl != null) {
                return Optional.of(bl);
            }
        }

        return Optional.empty();
    }

    public Set<BlacklistEntity> findAllBl(String identValue, IdentType identType) {
        Set<Long> linkedIdentIds = identService.findAllLinkedIdentIds(identValue, identType);
        return linkedIdentIds
                .stream()
                .map(blacklistRepo::findByIdentId)
                .collect(Collectors.toSet());
    }
}
