package bynull.realty.services;

import bynull.realty.components.FacebookHelperComponent;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.Authority;
import bynull.realty.data.business.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.UUID;

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
    FacebookHelperComponent facebookHelperComponent;

    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User with username '"+username+"' not found");
        }
        return user;
    }

    @Transactional
    @Override
    public UsernameTokenPair authenticateFacebookUser(String facebookId, String accessToken) {
        try {
            FacebookHelperComponent.FacebookVerificationInfoDTO verify = facebookHelperComponent.verify(new FacebookHelperComponent.ClientShortInfo(facebookId, accessToken));
            User user = userRepository.findByFacebookId(verify.facebookId);
            if(user == null) {
                LOGGER.debug("No user found that matches facebook id. Creating new one.");
                user = new User();
//                TODO: get more details from FB if possible to fill account in a proper way
//                user.setFirstName(verify.name);
//                user.setLastName(verify.name);
                user.setEmail(verify.email);
                user.setUsername("user_" + UUID.randomUUID());
                String rawPass = "password" + UUID.randomUUID();
                LOGGER.info("Generated password for user with fb id [{}]: []", verify.facebookId, rawPass);
                user.setPasswordHash(passwordEncoder.encodePassword(rawPass, null));
                user.setFacebookId(verify.facebookId);
                user.addAuthority(new Authority(Authority.Name.ROLE_USER));
                user = userRepository.saveAndFlush(user);
            } else {
                LOGGER.debug("Found user from facebook");
            }
            String token = userTokenService.getTokenForUser(user);
            return new UsernameTokenPair(user.getUsername(), token);
        } catch (FacebookHelperComponent.FacebookAuthorizationException e) {
            LOGGER.error("Exception occurred while trying to authorize");
            throw new AuthorizationServiceException("Unable to authorize with facebook", e);
        }
    }
}
