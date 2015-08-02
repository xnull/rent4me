package bynull.realty.dao.api.ident;

import bynull.realty.data.business.ids.IdRelationEntity;
import bynull.realty.data.business.ids.IdentType;
import bynull.realty.data.business.ids.IdentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by null on 7/27/15.
 */
public interface IdentRepository extends JpaRepository<IdentEntity, Long> {

    IdentEntity findByValueAndType(String ident, String type);
}
