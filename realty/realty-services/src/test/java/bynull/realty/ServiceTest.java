package bynull.realty;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dionis on 02/01/15.
 */
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
@ContextConfiguration(locations = {"classpath:service-test-context.xml"})
public abstract class ServiceTest extends SpringTest {
}
