package bynull.realty.dao.geo;

import bynull.realty.DbTest;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.data.common.GeoPoint;
import org.junit.Test;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class CityRepositoryTest extends DbTest {

    @PersistenceContext
    EntityManager em;

    @Resource
    CityRepository cityRepository;

    @Test
    public void persistAndGetTest() throws Exception {
        CountryEntity country = new CountryEntity();
        country.setName("Estonia");
        em.persist(country);

        CityEntity city = new CityEntity();
        city.setName("Tallinn");
        BoundingBox area = new BoundingBox();
        area.setHigh(new GeoPoint(1,1));
        area.setLow(new GeoPoint(-1, -1));
        city.setArea(area);
        city.setCountry(country);
        city = cityRepository.saveAndFlush(city);
        em.clear();

        CityEntity found = cityRepository.findOne(city.getId());
        assertThat(found, is(notNullValue()));
        assertThat(found.getName(), is("Tallinn"));
        assertThat(found.getCountry(), is(notNullValue()));
        assertThat(found.getCountry(), is(country));
        assertThat(found.getArea(), is(area));
    }

    @Test
    public void findCityByPoint_1_Found() throws Exception {
        CountryEntity country = new CountryEntity();
        country.setName("Estonia");
        em.persist(country);

        CityEntity city = new CityEntity();
        city.setName("Tallinn");
        BoundingBox area = new BoundingBox();
        area.setHigh(new GeoPoint(1,1));
        area.setLow(new GeoPoint(-1, -1));
        city.setArea(area);
        city.setCountry(country);
        city = cityRepository.saveAndFlush(city);

        CityEntity found = cityRepository.findByPoint(0.0, 0.0);
        assertThat(found, is(city));
    }

    @Test
    public void findCityByPoint_2_Found() throws Exception {
        CountryEntity country = new CountryEntity();
        country.setName("Estonia");
        em.persist(country);

        CityEntity city = new CityEntity();
        city.setName("Tallinn");
        BoundingBox area = new BoundingBox();
        area.setHigh(new GeoPoint(1,1));
        area.setLow(new GeoPoint(-1, -1));
        city.setArea(area);
        city.setCountry(country);
        city = cityRepository.saveAndFlush(city);

        CityEntity found = cityRepository.findByPoint(2.0, 2.0);
        assertThat(found, is(nullValue()));
    }
}