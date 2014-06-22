package bynull.realty;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dionis on 22/06/14.
 */
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
public class DbTest extends SpringTest {
}
