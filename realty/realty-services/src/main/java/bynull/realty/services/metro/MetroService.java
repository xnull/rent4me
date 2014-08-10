package bynull.realty.services.metro;

import bynull.realty.utils.JsonUtils;
import bynull.realty.utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by null on 10.08.14.
 */
@Service
public class MetroService {
    private static final Logger LOG = LoggerFactory.getLogger(MetroService.class);

    public MetroStationsDto getStations() throws MetroServiceException {
        LOG.debug("Get stations of the metro");

        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://yastatic.net/metro/2.1.9-8/data/").path("1.ru.svg");
            SvgMetro result = XmlUtils.stringToObject(target.request(MediaType.APPLICATION_SVG_XML_TYPE).get(String.class), SvgMetro.class);

            String stationsJsonString = result.getMetaData().getMetadata();
            return JsonUtils.fromJson(stationsJsonString, MetroStationsDto.class);
        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }
}
