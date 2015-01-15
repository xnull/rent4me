package bynull.realty;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dionis on 15/01/15.
 */
@Transactional
@TransactionConfiguration(transactionManager = "txManager")
@ContextConfiguration(locations = {"classpath:vk-test-context.xml"})
public class VkTest extends SpringTest {
}
