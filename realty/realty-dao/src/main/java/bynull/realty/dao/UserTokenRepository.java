package bynull.realty.dao;

import bynull.realty.data.business.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dionis on 09/07/14.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
}
