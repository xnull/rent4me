package bynull.realty.services.api;

import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.dto.MetroDTO;
import bynull.realty.services.metro.MetroServiceException;

import java.util.List;

/**
 * Created by dionis on 3/13/15.
 */
public interface MetroService {

    List<? extends MetroDTO> findMetros(ApartmentRepositoryCustom.GeoParams geoParams);

    void syncMoscowMetrosWithDatabase() throws MetroServiceException;

    void syncStPetersburgMetrosWithDatabase() throws MetroServiceException;
}
