package bynull.realty.dao;

import bynull.realty.data.business.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dionis on 23/06/14.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(Authority.Name name);
}
