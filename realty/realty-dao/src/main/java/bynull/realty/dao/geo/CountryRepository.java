package bynull.realty.dao.geo;

import bynull.realty.data.common.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dionis on 19/01/15.
 */
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
}
