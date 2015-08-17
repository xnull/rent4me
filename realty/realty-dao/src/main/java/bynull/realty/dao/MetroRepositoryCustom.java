package bynull.realty.dao;

import bynull.realty.dao.apartment.ApartmentRepositoryCustom;
import bynull.realty.data.business.metro.MetroEntity;

import java.util.List;

/**
 * Created by dionis on 3/13/15.
 */
public interface MetroRepositoryCustom {
    List<MetroEntity> findMetros(ApartmentRepositoryCustom.GeoParams geoParams);
}
