package bynull.realty.services.impl;

import bynull.realty.components.VKHelperComponent;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.Authority;
import bynull.realty.data.business.User;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.AuthorityService;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.services.impl.socialnet.fb.FacebookHelperComponent;
import bynull.realty.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dionis on 23/06/14.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

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
            FacebookHelperComponent.ClientShortInfo person = new FacebookHelperComponent.ClientShortInfo(facebookId, accessToken);
            FacebookHelperComponent.FacebookVerificationInfoDTO verify = facebookHelperComponent.verify(person);
            FacebookHelperComponent.ExchangedToken exchangedToken = facebookHelperComponent.exchangeToken(person);

            log.info("Updated exchange token: [{}] expiration: [{}]", exchangedToken.accessToken, exchangedToken.liveTimeSeconds);
            
            User user = userRepository.findByFacebookId(verify.facebookId);
            if (user == null) {
                user = userRepository.findByEmail(verify.email);
                if (user == null) {
                    log.debug("No user found that matches facebook id or email. Creating new one.");
                    Authority authority = authorityService.findOrCreateAuthorityByName(Authority.Name.ROLE_USER);

                    user = new User();
//                TODO: get more details from FB if possible to fill account in a proper way
//                user.setFirstName(verify.name);
//                user.setLastName(verify.name);
                    user.setEmail(verify.email);
                    user.setUsername("user_" + UUID.randomUUID());
                    String rawPass = "password" + UUID.randomUUID();
                    log.info("Generated password for user with fb id [{}]: []", verify.facebookId, rawPass);
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
                log.debug("Found user from facebook");
            }
            user.setFbAccessToken(exchangedToken.accessToken);
            final Date expirationDateTime = new Date(System.currentTimeMillis() + (exchangedToken.liveTimeSeconds * 1000));
            user.setFbAccessTokenExpiration(expirationDateTime);
            user = userRepository.saveAndFlush(user);

            String token = userTokenService.getTokenForUser(user);
            return new UsernameTokenPair(user.getUsername(), token);
        } catch (FacebookHelperComponent.FacebookAuthorizationException e) {
            log.error("Exception occurred while trying to authorize", e);
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
                    log.debug("No user found that matches vkontakte id or email. Creating new one.");
                    Authority authority = authorityService.findOrCreateAuthorityByName(Authority.Name.ROLE_USER);

                    user = new User();
//                TODO: get more details from FB if possible to fill account in a proper way
//                user.setFirstName(verify.name);
//                user.setLastName(verify.name);
                    user.setEmail(verify.email);
                    user.setUsername("user_" + UUID.randomUUID());
                    String rawPass = "password" + UUID.randomUUID();
                    log.info("Generated password for user with vk id [{}]: []", verify.vkUserId, rawPass);
                    user.setPasswordHash(passwordEncoder.encodePassword(rawPass, null));


                    VKHelperComponent.VkUserInfo vkUserInfo = vkHelperComponent.retrieveMoreInfo(verify.vkUserId, verify.accessToken);
                    String lastName = vkUserInfo.getLastName();
                    String firstName = vkUserInfo.getFirstName();

                    user.setLastName(lastName);
                    user.setFirstName(firstName);

                    String displayName = StringUtils.trimToEmpty(firstName) + " " + StringUtils.trimToEmpty(lastName);
                    user.setDisplayName(StringUtils.trimToNull(displayName));
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
                log.debug("Found user from vkontakte");
            }
            String token = userTokenService.getTokenForUser(user);
            return new UsernameTokenPair(user.getUsername(), token);

        } catch (VKHelperComponent.VKAuthorizationException e) {
            log.error("Exception occurred while trying to authorize", e);
            throw new AuthorizationServiceException("Unable to authorize with facebook", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO getMyProfile() {
        User user = userRepository.getOne(SecurityUtils.getAuthorizedUser().getId());
        return UserDTO.from(user);
    }

    @Transactional
    @Override
    public boolean updateMyProfile(UserDTO dto) {
        User user = userRepository.findOne(SecurityUtils.getAuthorizedUser().getId());

        user.setDisplayName(dto.getDisplayName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        user = userRepository.saveAndFlush(user);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> findByName(String name) {
        Assert.notNull(name);
        PageRequest pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        if (name.isEmpty()) {
            return userRepository.findAll(pageable).getContent().stream().map(UserDTO::from).collect(Collectors.toList());
        }

        return userRepository.findByName("%" + name + "%", pageable).stream().map(UserDTO::from).collect(Collectors.toList());
    }
}
