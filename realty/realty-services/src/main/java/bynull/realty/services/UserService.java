package bynull.realty.services;

import bynull.realty.data.business.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

/**
 * @author dionis on 09/07/14.
 */
public interface UserService extends UserDetailsService {
    boolean isTokenValid(User user, String token);
    void createToken(User user, String token, Date expiration);
}
