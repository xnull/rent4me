package bynull.realty;

import bynull.realty.data.business.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by dionis on 02/01/15.
 */
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
@ContextConfiguration(locations = {"classpath:service-test-context.xml"})
public abstract class ServiceTest extends SpringTest {

    @PersistenceContext
    EntityManager entityManager;

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    public User createUser() {
        User user = new User();
        user.setUsername("user_" + UUID.randomUUID());
        user.setPasswordHash("hash");
        user.setEmail(user.getUsername() + "@fakemail.com");
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
