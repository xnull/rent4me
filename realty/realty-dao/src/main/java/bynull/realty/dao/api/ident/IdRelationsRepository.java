package bynull.realty.dao.api.ident;

import bynull.realty.data.business.ids.IdRelationEntity;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by null on 7/27/15.
 */
public interface IdRelationsRepository extends JpaRepository<IdRelationEntity, Long> {

    List<IdRelationEntity> findBySourceIdOrAdjacentId(Long sourceIdentId, Long adjacentIdentId);
    List<IdRelationEntity> findBySourceId(Long sourceIdentId);
    List<IdRelationEntity> findByAdjacentId(Long adjacentIdentId);
}
