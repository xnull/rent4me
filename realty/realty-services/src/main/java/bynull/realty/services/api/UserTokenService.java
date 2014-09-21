package bynull.realty.services.api;

import bynull.realty.data.business.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author dionis on 09/07/14.
 */
public interface UserTokenService {
    boolean isTokenValid(User user, String token);

    /**
     * Create a new token for user.
     *
     * @param user user for whom need to create new token.
     * @return newly created token.
     */
    String createToken(User user);

    /**
     * Get token if credentials are valid.
     *
     * @param username username
     * @param password password
     * @return newly generated token will be returned
     * @throws BadCredentialsException
     * @throws UsernameNotFoundException
     */
    String getTokenIfValidCredentials(String username, String password) throws BadCredentialsException, UsernameNotFoundException;

    /**
     * Receive a valid authentication token for user
     *
     * @param user User
     * @return Valid access token
     */
    String getTokenForUser(User user);

    void deleteToken(User user, String token);
}
