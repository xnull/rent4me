package bynull.realty.services.impl;

import bynull.realty.converters.CountryModelDTOConverter;
import bynull.realty.dao.geo.CountryRepository;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.dto.CountryDTO;
import bynull.realty.services.api.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by dionis on 3/21/15.
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Resource
    CountryRepository countryRepository;

    @Resource
    CountryModelDTOConverter countryConverter;

    @Transactional(readOnly = true)
    @Override
    public CountryDTO findByName(String name) {
        CountryEntity country = countryRepository.findByNameIgnoreCase(name);
        Assert.notNull(country);
        return countryConverter.toTargetType(country);
    }
}
