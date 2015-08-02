package bynull.realty.dao.blacklist;

import bynull.realty.data.business.blacklist.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by null on 7/27/15.
 */
public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {

    BlacklistEntity findByIdentId(Long identId);
}
