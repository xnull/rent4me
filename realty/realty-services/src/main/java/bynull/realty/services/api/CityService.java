package bynull.realty.services.api;

import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 3/21/15.
 */
public interface CityService {
    List<? extends CityDTO> findByCountry(CountryDTO countryDTO);

    boolean create(CityDTO city);

    Optional<CityDTO> findById(long id);

    CityDTO update(CityDTO city);

    boolean delete(long id);

    List<? extends CityDTO> findAll();
}
