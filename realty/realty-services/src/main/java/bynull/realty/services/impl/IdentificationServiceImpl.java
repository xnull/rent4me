package bynull.realty.services.impl;

import bynull.realty.dao.api.ident.IdRelationsRepository;
import bynull.realty.dao.api.ident.IdentRepository;
import bynull.realty.data.business.ids.IdRelationEntity;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by null on 7/31/15.
 */
@Service
@Slf4j
public class IdentificationServiceImpl {

    @Resource
    private IdentRepository idRepo;

    @Resource
    private IdRelationsRepository idRelationsRepo;

    public Optional<IdentEntity> find(Long id) {
        return Optional.ofNullable(idRepo.findOne(id));
    }

    public Optional<IdentEntity> find(String identValue, IdentType identType) {
        return Optional.ofNullable(idRepo.findByValueLikeAndType(identValue, identType.getType()));
    }

    public IdentEntity findAndSaveIfNotExists(String identId, IdentType identType) {
        return find(identId, identType).orElseGet(() -> save(identId, identType));
    }

    /**
     * Соединить все идентификаторы между собой.
     * Для этого необходимо
     * 1. найти в базе все связанные с sourceIdent идентификаторы
     * 2. пройти по списку adjacentIdents и сравнить со смиском имеющихся идентов из п.1
     * 3. добавить adjacentIdents в adjacentIdents которых ещё нет имеющемся графе полученном в п.1.
     *
     * @param sourceIdent
     * @param adjacentIdents
     */
    public void mergeIdents(IdentEntity sourceIdent, Set<Long> adjacentIdents) {
        Set<Long> dbAdjacentIds = findAllLinkedIdentIds(sourceIdent);

        Set<Long> newAdjacent = adjacentIdents.stream()
                .filter(adjId -> !dbAdjacentIds.contains(adjId))
                .collect(Collectors.toSet());

        for (Long adjId : newAdjacent) {
            IdRelationEntity rel = new IdRelationEntity();
            rel.setSourceId(sourceIdent.getId());
            rel.setAdjacentId(adjId);

            idRelationsRepo.save(rel);
        }
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
        Set<Long> sourceAdj = idRelationsRepo.findBySourceId(identId)
                .stream()
                .map(IdRelationEntity::getAdjacentId)
                .collect(Collectors.toSet());

        Set<Long> targetAdj = idRelationsRepo.findByAdjacentId(identId)
                .stream()
                .map(IdRelationEntity::getSourceId)
                .collect(Collectors.toSet());

        sourceAdj.addAll(targetAdj);

        return sourceAdj;
    }

    public IdentEntity save(String value, IdentType type) {
        IdentEntity ident = new IdentEntity();
        ident.setValue(value);
        ident.setType(type.getType());

        return idRepo.save(ident);
    }

    public Set<IdentEntity> findAllLinkedIdents(String identValue, IdentType identType) {
        return findAllLinkedIdentIds(identValue, identType)
                .stream()
                .map(idRepo::findOne)
                .collect(Collectors.toSet());
    }
}
