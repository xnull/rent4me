package bynull.realty.dao;

import bynull.realty.data.business.User;
import bynull.realty.data.business.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author dionis on 09/07/14.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    @Query("select (case when count(t) > 0 then true else false end) from UserToken t where t.user=:user and t.token=:token")
    boolean isValidToken(@Param("user")User user, @Param("token") String token);
    UserToken findByUser(User user);
    UserToken findByUserAndToken(User user, String token);
}
