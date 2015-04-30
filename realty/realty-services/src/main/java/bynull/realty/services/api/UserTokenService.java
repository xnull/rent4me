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
     * @param email username
     * @param password password
     * @return newly generated token will be returned
     * @throws BadCredentialsException
     * @throws UsernameNotFoundException
     */
    UserService.UsernameTokenPair getUsernameAndTokenIfValidCredentials(String email, String password) throws BadCredentialsException, UsernameNotFoundException;

    /**
     * Checks whether provided authentications is valid username token pair
     * @param usernameTokenPair
     * @return
     */
    boolean isValidAuthentication(UserService.UsernameTokenPair usernameTokenPair);

    /**
     * Receive a valid authentication token for user
     *
     * @param user User
     * @return Valid access token
     */
    String getTokenForUser(User user);

    void deleteToken(User user, String token);
}
