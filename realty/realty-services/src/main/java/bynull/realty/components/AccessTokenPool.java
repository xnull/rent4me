package bynull.realty.components;

import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.User;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dionis on 28/01/15.
 */
@Component
public class AccessTokenPool {
    @Resource
    UserRepository userRepository;
    private List<String> validVKEmails = ImmutableList.of("diostm@gmail.com");
    private List<String> validFBEmails = ImmutableList.of("dionis.argiri@gmail.com");

    public String getValidFbAccessToken() {
        User user = userRepository.findByEmailIn(validFBEmails).iterator().next();
        String fbAccessToken = user.getFbAccessToken();
        Assert.notNull(fbAccessToken);
        return fbAccessToken;
    }

    public String getValidVkAccessToken() {
        User user = userRepository.findByEmailIn(validVKEmails).iterator().next();
        String vkAccessToken = user.getVkAccessToken();
        Assert.notNull(vkAccessToken);
        return vkAccessToken;
    }
}
