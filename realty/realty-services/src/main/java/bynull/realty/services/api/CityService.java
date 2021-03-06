package bynull.realty.services.api;

import bynull.realty.data.common.GeoPoint;
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

    Optional<CityDTO> update(CityDTO city);

    boolean delete(long id);

    List<? extends CityDTO> findAll();

    Optional<CityDTO> findByGeoPoint(Optional<GeoPoint> geoPoint);

    Optional<CityDTO> getMoscow();
}
