package bynull.realty.services.impl;

import bynull.realty.converters.CityModelDTOConverter;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.dao.geo.CountryRepository;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import bynull.realty.services.api.CityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 3/21/15.
 */
@Service
public class CityServiceImpl implements CityService {
    @Resource
    CityModelDTOConverter cityModelDTOConverter;

    @Resource
    CountryRepository countryRepository;

    @Resource
    CityRepository cityRepository;

    @Transactional(readOnly = true)
    @Override
    public List<? extends CityDTO> findByCountry(CountryDTO countryDTO) {
        Assert.notNull(countryDTO);
        Assert.notNull(countryDTO.getId());

        CountryEntity country = countryRepository.findOne(countryDTO.getId());
        Assert.notNull(country);

        List<CityEntity> cities = cityRepository.findByCountry(country);

        return cityModelDTOConverter.toTargetList(cities);
    }

    @Transactional
    @Override
    public boolean create(CityDTO city) {
        Assert.notNull(city);
        Assert.notNull(city.getCountry());
        Assert.notNull(city.getCountry().getId());

        CountryEntity country = countryRepository.findOne(city.getCountry().getId());
        Assert.notNull(country);

        CityEntity found = cityRepository.findByCountryAndNameIgnoreCase(country, city.getName());
        if (found != null) {
            return false;
        }
        CityEntity newEntry = cityModelDTOConverter.toSourceType(city);

        newEntry = cityRepository.saveAndFlush(newEntry);

        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CityDTO> findById(long id) {
        return cityModelDTOConverter.toTargetType(Optional.ofNullable(cityRepository.findOne(id)));
    }

    @Transactional
    @Override
    public Optional<CityDTO> update(CityDTO city) {
        CityEntity cityEntity = cityModelDTOConverter.toSourceType(city);

        Optional<CityEntity> found = Optional.ofNullable(cityRepository.findOne(city.getId()));

        return found.flatMap(c -> {
            c.setName(cityEntity.getName());
            c.setArea(cityEntity.getArea());
            c.setCountry(cityEntity.getCountry());
            c = cityRepository.saveAndFlush(c);

            return cityModelDTOConverter.toTargetType(Optional.of(c));
        });
    }

    @Transactional
    @Override
    public boolean delete(long id) {
        CityEntity city = cityRepository.findOne(id);
        if (city != null) {
            cityRepository.delete(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends CityDTO> findAll() {
        return cityModelDTOConverter.toTargetList(cityRepository.findAll(new Sort("name")));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CityDTO> findByGeoPoint(Optional<GeoPoint> geoPoint) {
        return geoPoint.flatMap(g -> {
            CityEntity cityEntity = cityRepository.findByPoint(geoPoint.get().getLongitude(), geoPoint.get().getLatitude());
            return cityModelDTOConverter.toTargetType(Optional.ofNullable(cityEntity));
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CityDTO> getMoscow() {
        GeoPoint cityCenterPoint = MetroServiceImpl.MOSCOW_CITY_DESCRIPTION.getCityCenterPoint();
        CityEntity cityEntity = cityRepository.findByPoint(cityCenterPoint.getLongitude(), cityCenterPoint.getLatitude());
        Assert.notNull(cityEntity, "Moscow entity should be present");
        return cityModelDTOConverter.toTargetType(Optional.of(cityEntity));
    }
}
