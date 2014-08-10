package bynull.realty.services.maps.google;

import bynull.realty.services.metro.MetroService;
import bynull.realty.services.metro.MetroServiceException;
import bynull.realty.services.metro.MetroStationsDto;
import bynull.realty.services.metro.SvgMetro;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.utils.XmlParseException;
import bynull.realty.utils.XmlUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by null on 10.08.14.
 */
public class TestSubwayStations {

    /**
     * Metro stations, svg format: http://yastatic.net/metro/2.1.9-8/data/1.ru.svg
     */
    @Test
    public void test() throws MetroServiceException {
        MetroService metroService = new MetroService();
        MetroStationsDto stationsJson = metroService.getStations();

        assertTrue(stationsJson.getStations().size() > 0);
        assertEquals(stationsJson.getStationCount(), stationsJson.getStations().size());
        assertEquals("Улица Подбельского", stationsJson.getStations().get("1").getName());

        assertTrue(stationsJson.getLines().size() > 0);
        assertEquals(stationsJson.getLineCount(), stationsJson.getLines().size());
        assertEquals("Сокольническая линия", stationsJson.getLines().get("1").getName());
    }
}
