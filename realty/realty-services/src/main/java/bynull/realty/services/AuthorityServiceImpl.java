package bynull.realty.services;

import bynull.realty.dao.AuthorityRepository;
import bynull.realty.data.business.Authority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @author dionis on 10/07/14.
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    AuthorityRepository authorityRepository;

    @Transactional
    @Override
    public Authority findOrCreateAuthorityByName(Authority.Name name) {
        Assert.notNull(name);
        Authority found = authorityRepository.findByName(name);
        if (found == null) {
            Authority authority = new Authority(name);
            authority = authorityRepository.saveAndFlush(authority);
            return authority;
        } else {
            return found;
        }
    }
}
