package bynull.realty.components;

import bynull.realty.dao.AuthorityRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.Authority;
import bynull.realty.data.business.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * @author dionis on 23/06/14.
 */
@Component
public class BootStrapComponent implements InitializingBean {
    @Resource
    AuthorityRepository authorityRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    TransactionOperations transactionOperations;
    @Resource
    Md5PasswordEncoder md5PasswordEncoder;


    @Override
    public void afterPropertiesSet() throws Exception {
        if(1==1) return;
        authorityRepository.deleteAll();
        userRepository.deleteAll();
        transactionOperations.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Authority userAuthority = authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_USER));
                Authority adminAuthority = authorityRepository.saveAndFlush(new Authority(Authority.Name.ROLE_ADMIN));
                {
                    User user = new User();
                    user.setUsername("user");
                    user.setPasswordHash(md5PasswordEncoder.encodePassword("123", null));
                    user.addAuthority(userAuthority);
                    user = userRepository.saveAndFlush(user);
                }

                {
                    User user = new User();
                    user.setUsername("admin");
                    user.setPasswordHash(md5PasswordEncoder.encodePassword("123", null));
                    user.addAuthority(adminAuthority);
                    user = userRepository.saveAndFlush(user);
                }
            }
        });
    }
}
