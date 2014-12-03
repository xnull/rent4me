package bynull.realty.services.impl;

import bynull.realty.components.FacebookHelperComponent;
import bynull.realty.components.VKHelperComponent;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.Authority;
import bynull.realty.data.business.User;
import bynull.realty.services.api.AuthorityService;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.ws.rs.NotAuthorizedException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author dionis on 23/06/14.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserRepository userRepository;

    @Resource
    UserTokenService userTokenService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    AuthorityService authorityService;

    @Resource
    FacebookHelperComponent facebookHelperComponent;

    @Resource
    VKHelperComponent vkHelperComponent;

    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' not found");
        }
        return user;
    }

    @Transactional
    @Override
    public UsernameTokenPair authenticateFacebookUser(String facebookId, String accessToken) {
        try {
            FacebookHelperComponent.FacebookVerificationInfoDTO verify = facebookHelperComponent.verify(new FacebookHelperComponent.ClientShortInfo(facebookId, accessToken));
            User user = userRepository.findByFacebookId(verify.facebookId);
            if (user == null) {
                user = userRepository.findByEmail(verify.email);
                if(user == null) {
                    LOGGER.debug("No user found that matches facebook id or email. Creating new one.");
                    Authority authority = authorityService.findOrCreateAuthorityByName(Authority.Name.ROLE_USER);

                    user = new User();
//                TODO: get more details from FB if possible to fill account in a proper way
//                user.setFirstName(verify.name);
//                user.setLastName(verify.name);
                    user.setEmail(verify.email);
                    user.setUsername("user_" + UUID.randomUUID());
                    String rawPass = "password" + UUID.randomUUID();
                    LOGGER.info("Generated password for user with fb id [{}]: []", verify.facebookId, rawPass);
                    user.setPasswordHash(passwordEncoder.encodePassword(rawPass, null));
                    user.setDisplayName(verify.name);
                    user.setFirstName(verify.firstName);
                    user.setLastName(verify.lastName);
                    user.setAge(verify.birthday != null
                            ? (int) (TimeUnit.MILLISECONDS.convert(System.currentTimeMillis() - verify.birthday.getTime(), TimeUnit.DAYS) / 365.24)
                            : null);
                    user.addAuthority(authority);
                }
                user.setFacebookId(verify.facebookId);
                user = userRepository.saveAndFlush(user);
            } else {
                LOGGER.debug("Found user from facebook");
            }
            String token = userTokenService.getTokenForUser(user);
            return new UsernameTokenPair(user.getUsername(), token);
        } catch (FacebookHelperComponent.FacebookAuthorizationException e) {
            LOGGER.error("Exception occurred while trying to authorize", e);
            throw new AuthorizationServiceException("Unable to authorize with facebook", e);
        }
    }

    @Transactional
    @Override
    public UsernameTokenPair authenticateVkUser(String authCode) {
        try {
            VKHelperComponent.VKVerificationInfoDTO verify = vkHelperComponent.verify(new VKHelperComponent.ClientShortInfo(authCode));

            User user = userRepository.findByVkontakteId(verify.vkUserId);
            if (user == null) {
                user = userRepository.findByEmail(verify.email);

                if (user == null) {
                    LOGGER.debug("No user found that matches vkontakte id or email. Creating new one.");
                    Authority authority = authorityService.findOrCreateAuthorityByName(Authority.Name.ROLE_USER);

                    user = new User();
//                TODO: get more details from FB if possible to fill account in a proper way
//                user.setFirstName(verify.name);
//                user.setLastName(verify.name);
                    user.setEmail(verify.email);
                    user.setUsername("user_" + UUID.randomUUID());
                    String rawPass = "password" + UUID.randomUUID();
                    LOGGER.info("Generated password for user with vk id [{}]: []", verify.vkUserId, rawPass);
                    user.setPasswordHash(passwordEncoder.encodePassword(rawPass, null));

                    //TODO: get more data from VK.
//                user.setDisplayName(verify.name);
//                user.setFirstName(verify.firstName);
//                user.setLastName(verify.lastName);
                    Date birthday = null;
                    user.setAge(birthday != null
                            ? (int) (TimeUnit.MILLISECONDS.convert(System.currentTimeMillis() - birthday.getTime(), TimeUnit.DAYS) / 365.24)
                            : null);
                    user.addAuthority(authority);
                }
                //update VK id here because user might have already other account.
                user.setVkontakteId(verify.vkUserId);
                user = userRepository.saveAndFlush(user);
//                userRepository.saveAndFlush(user);
            } else {
                LOGGER.debug("Found user from vkontakte");
            }
            String token = userTokenService.getTokenForUser(user);
            return new UsernameTokenPair(user.getUsername(), token);

        } catch (VKHelperComponent.VKAuthorizationException e) {
            LOGGER.error("Exception occurred while trying to authorize", e);
            throw new AuthorizationServiceException("Unable to authorize with facebook", e);
        }
    }
}
