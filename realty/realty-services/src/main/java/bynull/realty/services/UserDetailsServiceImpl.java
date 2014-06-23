package bynull.realty.services;

import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author dionis on 23/06/14.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User with username '"+username+"' not found");
        }
        return user;
    }
}
