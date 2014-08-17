package bynull.realty.services.metro;

import bynull.realty.services.metro.MetroSystemDto.MetroStationFullInfoDto;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.utils.XmlUtils;
import com.google.common.util.concurrent.Uninterruptibles;
import org.glassfish.jersey.SslConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;

import static bynull.realty.services.metro.MetroStationsDto.MetroStationDto;

/**
 * Created by null on 10.08.14.
 */
@Service
public class MetroService {
    private static final Logger LOG = LoggerFactory.getLogger(MetroService.class);

    public MetroSystemDto getStations() throws MetroServiceException {
        LOG.debug("Get stations of the metro");

        try {
            MetroStationsDto stationsDto = downloadStationsFromYandex();

            MetroSystemDto metroSystem = new MetroSystemDto();

            int x = 0;
            for (String stationId : stationsDto.getStations().keySet()) {
                x++; if (x > 50) break;
                MetroStationDto stationDto = stationsDto.getStations().get(stationId);
                GoogleStationInfo coords = getStationInfo(stationDto.getName());
                MetroStationFullInfoDto metro = new MetroStationFullInfoDto(coords, stationDto);

                metroSystem.addStation(metro);

                Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
            }

            return metroSystem;

        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }

    private MetroStationsDto downloadStationsFromYandex() throws bynull.realty.utils.XmlParseException, JsonMapperException {
        LOG.debug("Download the stations from yandex");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://yastatic.net/metro/2.1.9-8/data/").path("1.ru.svg");
        SvgMetro result = XmlUtils.stringToObject(target.request(MediaType.APPLICATION_SVG_XML_TYPE).get(String.class), SvgMetro.class);

        String stationsJsonString = result.getMetaData().getMetadata();
        return JsonUtils.fromJson(stationsJsonString, MetroStationsDto.class);
    }

    /**
     * metro savelovskaya: https://maps.googleapis.com/maps/api/place/textsearch/json?query=метро+савеловская&sensor=true&key=AIzaSyD8FthnabTSsiQh9N6QTKGqePNTOEZOhXU
     */
    public GoogleStationInfo getStationInfo(String metroStation) throws MetroServiceException {
        LOG.debug("Get station info from google: {}", metroStation);

        SslConfigurator sslConfig = SslConfigurator.newInstance();
        SSLContext sslContext = sslConfig.createSSLContext();
        Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
        WebTarget target = client.target("https://maps.googleapis.com/").path("maps/api/place/textsearch/json");
        //target = target.queryParam("query", "метро+" + metroStation.replaceAll(" ", "+"));
        target = target.queryParam("query", "город+Москва,+Москва,+метро+" + metroStation.replaceAll(" ", "+"));
        target = target.queryParam("sensor", true);
        target = target.queryParam("key", "AIzaSyCrj3YUycdJcFUzxQHUja3f4VbrgKv_cIM");
        //target = target.queryParam("key", "AIzaSyD8FthnabTSsiQh9N6QTKGqePNTOEZOhXU");

        String response = target.request(MediaType.TEXT_HTML_TYPE).get(String.class);
        try {
            GoogleStationInfo result = JsonUtils.fromJson(response, GoogleStationInfo.class);
            if (result.getResults().isEmpty()){
                throw new MetroServiceException("Error loading data: " + mapToErrorDto(response).getErrorMessage());
            }
            return result;
        } catch (JsonMapperException e) {
            throw new MetroServiceException(e);
        }

    }

    private GoogleApiErrorDto mapToErrorDto(String json) throws JsonMapperException {
        return JsonUtils.fromJson(json, GoogleApiErrorDto.class);
    }

    public MetroStationsDto getStationsFromYandex() throws MetroServiceException {
        try {
            return downloadStationsFromYandex();
        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }
}
