package bynull.realty.services.api;

import bynull.realty.dto.CountryDTO;

/**
 * Created by dionis on 3/21/15.
 */
public interface CountryService {
    CountryDTO findByName(String name);
}
