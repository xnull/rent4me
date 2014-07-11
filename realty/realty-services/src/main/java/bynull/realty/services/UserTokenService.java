package bynull.realty.services;

import bynull.realty.data.business.User;
import bynull.realty.data.business.UserToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

/**
 * @author dionis on 09/07/14.
 */
public interface UserTokenService {
    boolean isTokenValid(User user, String token);

    /**
     * Create a new token for user.
     * @param user
     * @return
     */
    String createToken(User user);

    /**
     * Get token if credentials are valid.
     * @param username username
     * @param password password
     * @return newly generated token will be returned
     * @throws BadCredentialsException
     * @throws UsernameNotFoundException
     */
    String getTokenIfValidCredentials(String username, String password) throws BadCredentialsException, UsernameNotFoundException;

    /**
     * Receive a valid authentication token for user
     * @param user User
     * @return Valid access token
     */
    String getTokenForUser(User user);

    void deleteToken(User user, String token);
}
