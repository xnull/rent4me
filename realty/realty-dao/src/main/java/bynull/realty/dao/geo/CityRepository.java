package bynull.realty.dao.geo;

import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Vyacheslav Petc
 * @since 1/9/15.
 */
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    CityEntity findByNameAndCountry_Name(String name, String countryName);

    @Query("select c from CityEntity c where c.country=:country order by name desc")
    List<CityEntity> findByCountry(@Param("country") CountryEntity country);

    @Query("select c from CityEntity c where c.country=:country and lower(c.name)=lower(:name)")
    CityEntity findByCountryAndNameIgnoreCase(@Param("country")CountryEntity country, @Param("name")String name);

    @Query(value = "select c.* from cities c where c.area ~ ST_GeomFromText( concat('SRID=4326;POINT('," +
            ":lat," +
                    "' '," +
                    ":lng," +
                    "')'))", nativeQuery = true)
    CityEntity findByPoint(@Param("lat")double lat, @Param("lng")double lng);
}
