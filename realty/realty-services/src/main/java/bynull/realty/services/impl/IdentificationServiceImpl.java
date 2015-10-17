package bynull.realty.services.impl;

import bynull.realty.dao.api.ident.IdRelationsRepository;
import bynull.realty.dao.api.ident.IdentRepository;
import bynull.realty.dao.api.ident.IdentRepository.IdentRepositorySimple;
import bynull.realty.dao.util.IdentRefiner;
import bynull.realty.data.business.ids.IdRelationEntity;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ContactDTO;
import bynull.realty.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by null on 7/31/15.
 */
@Service
@Slf4j
public class IdentificationServiceImpl {

    @Resource
    private IdentRepository idRepo;

    @Resource
    private IdentRepositorySimple idRepoImpl;

    @Resource
    private IdRelationsRepository idRelationsRepo;

    public Optional<IdentEntity> find(Long id) {
        return Optional.ofNullable(idRepo.findOne(id));
    }

    public Optional<IdentEntity> find(String identValue, IdentType identType) {
        log.trace("Find an ident: {}, {}", identValue, identType);
        return idRepoImpl.find(IdentRefiner.refine(identValue, identType), identType.getType());
    }

    public IdentEntity findAndSaveIfNotExists(String identValue, IdentType identType) {
        log.debug("Find and save an ident if not exists: {}, {}", identValue, identType);
        Optional<IdentEntity> ident = find(IdentRefiner.refine(identValue, identType), identType);
        if(ident.isPresent()){
            return ident.get();
        }

        return save(IdentRefiner.refine(identValue, identType), identType);
    }

    /**
     * Соединить все идентификаторы между собой.
     * Для этого необходимо
     * 1. найти в базе все связанные с sourceIdent идентификаторы
     * 2. пройти по списку adjacentIdents и сравнить со смиском имеющихся идентов из п.1
     * 3. добавить adjacentIdents в adjacentIdents которых ещё нет имеющемся графе полученном в п.1.
     *
     *  @param sourceIdent идент с которым связываем другие иденты
     * @param adjacentIdents связанные иденты
     * @return список новых связанных ид с которыми в результате операции был связан sourceIdent
     */
    private Set<Long> linkIdents(IdentEntity sourceIdent, Set<Long> adjacentIdents) {
        log.debug("Merge idents. Source: {}, adjacent ids: {}", sourceIdent, adjacentIdents);
        Set<Long> existsAdjacentIds = findAllLinkedIdentIds(sourceIdent);

        Set<Long> newAdjacent = adjacentIdents.stream()
                .filter(adjId -> !existsAdjacentIds.contains(adjId))
                .collect(Collectors.toSet());

        for (Long adjId : newAdjacent) {
            IdRelationEntity rel = new IdRelationEntity();
            rel.setSourceId(sourceIdent.getId());
            rel.setAdjacentId(adjId);

            idRelationsRepo.save(rel);
        }

        return newAdjacent;
    }

    public Set<Long> findAllLinkedIdentIds(String identValue, IdentType identType) {
        Optional<IdentEntity> sourceIdent = find(identValue, identType);
        if (sourceIdent.isPresent()){
            Set<Long> linked = findAllLinkedIdentIds(sourceIdent.get());
            linked.add(sourceIdent.get().getId());
            return linked;
        }

        return Collections.emptySet();
    }

    /**
     * Поиском в ширину по графу ищем все идентификаторы связанные с sourceIdent
     *
     * @param sourceIdent
     * @return
     */
    public Set<Long> findAllLinkedIdentIds(IdentEntity sourceIdent) {
        Set<Long> result = new HashSet<>();
        result.add(sourceIdent.getId());

        Queue<Long> queue = new ArrayDeque<>();
        queue.add(sourceIdent.getId());

        while (!queue.isEmpty()) {
            Long currentIdent = queue.poll();

            for (Long adjVertex : findAdjacent(currentIdent)) {
                if (!result.contains(adjVertex)) { //для каждой непомеченной смежной вершины
                    result.add(adjVertex);
                    queue.add(adjVertex);
                }
            }
        }

        return  result;
    }

    private Set<Long> findAdjacent(Long identId) {
        Set<Long> sourceAdj = new HashSet<>();
        for (IdRelationEntity relationEntity : idRelationsRepo.findBySourceIdOrAdjacentId(identId, identId)) {
            sourceAdj.add(relationEntity.getAdjacentId());
            sourceAdj.add(relationEntity.getSourceId());
        }

        return sourceAdj;
    }

    public IdentEntity save(String value, IdentType type) {
        IdentEntity ident = new IdentEntity();
        ident.setIdentValue(IdentRefiner.refine(value, type));
        ident.setIdentType(type.getType());

        Optional<IdentEntity> dbIdent = idRepoImpl.find(value, type.getType());
        if (!dbIdent.isPresent()) {
            return idRepo.save(ident);
        }
        return dbIdent.get();
    }

    public Set<IdentEntity> findAllLinkedIdents(String identValue, IdentType identType) {
        return findAllLinkedIdentIds(identValue, identType)
                .stream()
                .map(idRepo::findOne)
                .collect(Collectors.toSet());
    }


    /**
     * Связать имеющиеся идентификаторы
     *
     * @param sourceIdent
     * @param apartment
     */
    public Set<Long> mergeIdents(IdentEntity sourceIdent, ApartmentDTO apartment) {
        /**
         * 1. Находим все идентификаторы с которыми связаны данные апартаменты
         * 2. связываем все иденты
         * 3. добавляем ссылку на ids в черный список
         */
        switch (apartment.getDataSource()) {
            case INTERNAL:
                return linkIdents(sourceIdent, getInternalApartmentIdents(apartment.getOwner()));
            case FACEBOOK:
                return linkIdents(sourceIdent, getSocialNetIdents(apartment, IdentType.FB_ID));
            case VKONTAKTE:
                return linkIdents(sourceIdent, getSocialNetIdents(apartment, IdentType.VK_ID));
        }
        return Collections.emptySet();
    }

    private Set<Long> getSocialNetIdents(ApartmentDTO apartment, IdentType type) {
        Set<Long> adjIdents = new HashSet<>();
        adjIdents.add(findAndSaveIfNotExists(apartment.getAuthorId(), type).getId());

        for (ContactDTO contact : apartment.getContacts()) {
            switch (contact.getType()) {
                case PHONE:
                    try {
                        adjIdents.add(findAndSaveIfNotExists(
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
        result.add(findAndSaveIfNotExists(user.getId().toString(), IdentType.USER_ID).getId());
        result.add(findAndSaveIfNotExists(user.getEmail(), IdentType.EMAIL).getId());
        result.add(findAndSaveIfNotExists(user.getFacebookId(), IdentType.FB_ID).getId());
        result.add(findAndSaveIfNotExists(user.getVkontakteId(), IdentType.VK_ID).getId());
        result.add(findAndSaveIfNotExists(user.getPhoneNumber(), IdentType.PHONE).getId());

        return result;
    }
}
