package bynull.realty;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author dionis on 22/06/14.
 */
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class DbTest extends SpringTest {
    @PersistenceContext
    EntityManager entityManager;

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
