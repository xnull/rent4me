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

        return identService.mergeIdents(aptIdent, optApt.get())
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
        return identService.findAndSaveIfNotExists(apartmentId.toString(), IdentType.APARTMENT);
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
