package bynull.realty.dao.geo;

import bynull.realty.data.common.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by dionis on 19/01/15.
 */
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    @Query("select c from CountryEntity c where lower(c.name)=lower(:name)")
    CountryEntity findByNameIgnoreCase(@Param("name") String name);
}
