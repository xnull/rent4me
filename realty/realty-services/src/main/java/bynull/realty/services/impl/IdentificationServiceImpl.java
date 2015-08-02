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

    public IdentEntity find(String identId, IdentType identType) {
        return idRepo.findByValueAndType(identId, identType.getType());
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
        Set<Long> dbAdjacentIds = findAllLinkedIdents(sourceIdent);

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

    /**
     * Поиском в ширину по графу ищем все идентификаторы связанные с sourceIdent
     *
     * @param sourceIdent
     * @return
     */
    private Set<Long> findAllLinkedIdents(IdentEntity sourceIdent) {
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

        return result;
    }

    private List<Long> findAdjacent(Long identId) {
        return idRelationsRepo.findBySourceId(identId)
                .stream()
                .map(IdRelationEntity::getAdjacentId)
                .collect(Collectors.toList());
    }

    public IdentEntity save(String value, IdentType type) {
        IdentEntity ident = new IdentEntity();
        ident.setValue(value);
        ident.setType(type.getType());

        return idRepo.save(ident);
    }
}
