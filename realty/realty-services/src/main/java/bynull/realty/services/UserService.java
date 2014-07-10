package bynull.realty.services;

import bynull.realty.data.business.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

/**
 * @author dionis on 09/07/14.
 */
public interface UserService extends UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    class UsernameTokenPair {
        public final String username;
        public final String token;

        public UsernameTokenPair(String username, String token) {
            this.username = username;
            this.token = token;
        }
    }
    UsernameTokenPair authenticateFacebookUser(String facebookId, String accessToken);
}
