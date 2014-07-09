package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.User;
import bynull.realty.data.business.UserToken;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserTokenRepositoryTest extends DbTest {
    @Resource
    UserTokenRepository userTokenRepository;

    @Test
    public void createNewToken() {
        User user = createUser();

        UserToken userToken = new UserToken();
        userToken.setUser(user);
        userToken.setToken("token");

        userToken = userTokenRepository.saveAndFlush(userToken);
        flushAndClear();

        assertThat(userToken.getId(), is(notNullValue()));

        UserToken persisted = userTokenRepository.findOne(userToken.getId());

        assertThat(persisted, is(notNullValue()));
        assertThat(persisted.getCreated(), is(notNullValue()));
        assertThat(persisted.getUpdated(), is(notNullValue()));
        assertThat(persisted.getUpdated(), equalTo(persisted.getCreated()));
    }
}