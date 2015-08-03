package bynull.realty.services.api;

import bynull.realty.dto.CountryDTO;

import java.util.Optional;

/**
 * Created by dionis on 3/21/15.
 */
public interface CountryService {
    Optional<CountryDTO> findByName(String name);
}
