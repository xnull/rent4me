package bynull.realty.services.impl;

import bynull.realty.dao.UserRepository;
import bynull.realty.dao.UserTokenRepository;
import bynull.realty.data.business.User;
import bynull.realty.data.business.UserToken;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.NotFoundException;
import java.util.UUID;

/**
 * @author dionis on 09/07/14.
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTokenServiceImpl.class);

    @Resource
    UserTokenRepository userTokenRepository;

    @Resource
    UserService userService;

    @Resource
    UserRepository userRepository;

    @Resource
    PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public boolean isTokenValid(User user, String token) {
        return userTokenRepository.isValidToken(user, token);
    }

    @Transactional
    @Override
    public String createToken(User user) {
        Assert.notNull(user);
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setUser(user);
        userToken = userTokenRepository.saveAndFlush(userToken);
        return userToken.getToken();
    }

    @Transactional
    @Override
    public UserService.UsernameTokenPair getUsernameAndTokenIfValidCredentials(String email, String password) throws BadCredentialsException, UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        boolean matches = user != null && passwordEncoder.isPasswordValid(user.getPasswordHash(), password, null);

        LOGGER.debug("Password matches? {}", matches);

        if (matches) {
            String token = getTokenForUser(user);
            return new UserService.UsernameTokenPair(user.getUsername(), token);
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Transactional
    @Override
    public String getTokenForUser(User user) {
        LOGGER.debug("Creating new token");
        String userToken = createToken(user);
        LOGGER.debug("Obtained new token [{}] for user [{}]", userToken, user.getId());
        return userToken;
    }

    @Transactional
    @Override
    public void deleteToken(User user, String token) {
        Assert.notNull(user);
        Assert.notNull(token);
        UserToken foundToken = userTokenRepository.findByUserAndToken(user, token);
        if (foundToken == null) {
            throw new NotFoundException();
        } else {
            userTokenRepository.delete(foundToken);
        }
    }
}
