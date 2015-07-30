package bynull.realty.dao.blacklist;

import bynull.realty.data.business.blacklist.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by null on 7/27/15.
 */
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Blacklist findByApartmentId(Long apartmentId);
}
