package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.Authority;
import bynull.realty.data.business.User;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserRepositoryTest extends DbTest {
    @Resource
    UserRepository userRepository;

    @Resource
    AuthorityRepository authorityRepository;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setUsername("dionis");
        user.setPasswordHash("hash");
        user.setEmail("a@b.c");
        User savedUser = userRepository.saveAndFlush(user);
        assertThat(savedUser, is(notNullValue()));
    }

    @Test
    public void createUserWithAuthorities() {
        User user = new User();
        user.setUsername("dionis");
        user.setPasswordHash("hash");
        user.setEmail("a@b.c");
        User savedUser = userRepository.saveAndFlush(user);
        authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_USER));
        flushAndClear();

        User found = userRepository.findOne(savedUser.getId());
        assertThat(found.getAuthorities(), is(empty()));
        Authority authority = authorityRepository.findByName(Authority.Name.ROLE_USER);
        assertThat(authority, is(notNullValue()));
        found.addAuthority(authority);
        userRepository.saveAndFlush(found);

        flushAndClear();

        User foundWithAuthorities = userRepository.findOne(savedUser.getId());
        assertThat(foundWithAuthorities.getAuthorities().size(), is(1));
    }

    @Test
    public void addingAuthorityMultipleTimesWontResultToDuplication() {
        User user = new User();
        user.setUsername("dionis");
        user.setPasswordHash("hash");
        user.setEmail("a@b.c");
        User savedUser = userRepository.saveAndFlush(user);
        authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_USER));
        flushAndClear();

        {
            User found = userRepository.findOne(savedUser.getId());
            assertThat(found.getAuthorities(), is(empty()));
            Authority authority = authorityRepository.findByName(Authority.Name.ROLE_USER);
            assertThat(authority, is(notNullValue()));
            found.addAuthority(authority);
            userRepository.saveAndFlush(found);

            flushAndClear();
        }

        {
            User found = userRepository.findOne(savedUser.getId());
            assertThat(found.getAuthorities(), is(not(empty())));
            Authority authority = authorityRepository.findByName(Authority.Name.ROLE_USER);
            assertThat(authority, is(notNullValue()));
            found.addAuthority(authority);
            userRepository.saveAndFlush(found);

            flushAndClear();
        }

        User foundWithAuthorities = userRepository.findOne(savedUser.getId());
        assertThat(foundWithAuthorities.getAuthorities().size(), is(1));
    }

    @Test
    public void removingAuthorities() {
        User user = new User();
        user.setUsername("dionis");
        user.setPasswordHash("hash");
        user.setEmail("a@b.c");
        User savedUser = userRepository.saveAndFlush(user);
        authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_USER));
        flushAndClear();

        {
            User found = userRepository.findOne(savedUser.getId());
            assertThat(found.getAuthorities(), is(empty()));
            Authority authority = authorityRepository.findByName(Authority.Name.ROLE_USER);
            assertThat(authority, is(notNullValue()));
            found.addAuthority(authority);
            userRepository.saveAndFlush(found);

            flushAndClear();
        }

        {
            User found = userRepository.findOne(savedUser.getId());
            assertThat(found.getAuthorities(), is(not(empty())));
            Authority authority = authorityRepository.findByName(Authority.Name.ROLE_USER);
            assertThat(authority, is(notNullValue()));
            found.removeAuthority(authority);
            userRepository.saveAndFlush(found);

            flushAndClear();
        }

        User foundWithAuthorities = userRepository.findOne(savedUser.getId());
        assertThat(foundWithAuthorities.getAuthorities(), is(empty()));
    }

    @Test
    public void findByEmailIgnoreCase() {
        User user = new User();
        user.setUsername("dionis");
        user.setPasswordHash("hash");
        user.setEmail("a@b.c");
        User savedUser = userRepository.saveAndFlush(user);
        authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_USER));
        flushAndClear();

        {
            User found = userRepository.findByEmail("a@b.c");
            assertThat(found, is(notNullValue()));
            assertThat(found.getId(), is(savedUser.getId()));
        }

        {
            User found = userRepository.findByEmail("A@B.C");
            assertThat(found, is(notNullValue()));
            assertThat(found.getId(), is(savedUser.getId()));
        }
    }
}