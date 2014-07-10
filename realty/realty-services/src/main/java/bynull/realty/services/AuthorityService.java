package bynull.realty.services;

import bynull.realty.data.business.Authority;

/**
 * @author dionis on 10/07/14.
 */
public interface AuthorityService {
    Authority findOrCreateAuthorityByName(Authority.Name name);
}
