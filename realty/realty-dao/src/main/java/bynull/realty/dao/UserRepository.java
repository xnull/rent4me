package bynull.realty.dao;

import bynull.realty.data.business.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dionis on 23/06/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByFacebookId(String facebookId);
}
