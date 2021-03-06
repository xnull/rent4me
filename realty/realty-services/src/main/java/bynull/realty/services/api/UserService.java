package bynull.realty.services.api;

import bynull.realty.data.business.User;
import bynull.realty.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * @author dionis on 09/07/14.
 */
public interface UserService extends UserDetailsService {
    @Override
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    UsernameTokenPair authenticateFacebookUser(String facebookId, String accessToken);

    UsernameTokenPair authenticateVkUser(String authCode);

    UserDTO getMyProfile();

    boolean updateMyProfile(UserDTO dto);

    List<UserDTO> findByName(String name);

    Optional<UserDTO> findByUsername(String name);

    UserDTO createNewProfile(UserDTO dto);

    class UsernameTokenPair {
        public final String username;
        public final String token;

        public UsernameTokenPair(String username, String token) {
            this.username = username;
            this.token = token;
        }
    }
}
