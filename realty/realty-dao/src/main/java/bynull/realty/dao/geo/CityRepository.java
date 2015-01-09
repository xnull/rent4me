package bynull.realty.dao.geo;

import bynull.realty.data.common.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vyacheslav Petc
 * @since 1/9/15.
 */
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    CityEntity findByNameAndCountry_Name(String name, String countryName);
}
