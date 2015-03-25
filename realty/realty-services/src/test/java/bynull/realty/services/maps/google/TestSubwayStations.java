package bynull.realty.services.maps.google;

import bynull.realty.services.impl.MetroServiceImpl;
import bynull.realty.services.metro.MetroServiceException;
import bynull.realty.services.metro.MetroStationsDto;
import bynull.realty.services.metro.MetroSystemDto;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by null on 10.08.14.
 */
public class TestSubwayStations {

    /**
     * Metro stations, svg format: http://yastatic.net/metro/2.1.9-8/data/1.ru.svg
     */
    @Test
    public void testGetStationsFromYandex() throws MetroServiceException {
        MetroServiceImpl moscowMetroSynchronisationService = new MetroServiceImpl();
        MetroStationsDto stationsJson = moscowMetroSynchronisationService.getStationsFromYandex(MetroServiceImpl.MOSCOW_CITY_DESCRIPTION);

        assertTrue(stationsJson.getStations().size() > 0);
        assertEquals(stationsJson.getStationCount(), stationsJson.getStations().size());
        assertEquals("Улица Подбельского", stationsJson.getStations().get("1").getName());

        assertTrue(stationsJson.getLines().size() > 0);
        assertEquals(stationsJson.getLineCount(), stationsJson.getLines().size());
        assertEquals("Сокольническая линия", stationsJson.getLines().get("1").getName());
    }

    @Ignore("It's working, but we don't need to check it every time, because of download from yandex and google")
    @Test
    public void testGetMetroSystem() throws MetroServiceException, JsonMapperException {
        MetroServiceImpl moscowMetroSynchronisationService = new MetroServiceImpl();
        MetroSystemDto stations = moscowMetroSynchronisationService.loadStations(MetroServiceImpl.MOSCOW_CITY_DESCRIPTION);

        //город Москва, Москва, Улица Подбельского
        JsonUtils.saveToFile(new File("metro-system.json"), stations);
    }
}
