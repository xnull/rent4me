package bynull.realty.dao;

import bynull.realty.data.business.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author dionis on 23/06/14.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByFacebookId(String facebookId);

    User findByVkontakteId(String vkontakteId);

    User findByEmail(String email);

    @Query("select u from User u where lower(u.displayName) like lower(:name)")
    List<User> findByName(@Param("name") String name, Pageable   pageable);
}
