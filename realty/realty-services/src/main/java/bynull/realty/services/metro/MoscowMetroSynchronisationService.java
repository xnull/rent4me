package bynull.realty.services.metro;

import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.services.metro.MetroSystemDto.MetroStationFullInfoDto;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.utils.XmlUtils;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.SslConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static bynull.realty.services.metro.MetroStationsDto.MetroStationDto;

/**
 * @author Vyacheslav Petc
 * @since 10.08.14.
 */
@Service
@Lazy
@Slf4j
public class MoscowMetroSynchronisationService {
    private static final String MOSCOW = "Москва";
    private static final String RUSSIA = "Россия";

    @Autowired
    private MetroRepository metroRepository;
    @Autowired
    private CityRepository cityRepository;

    /**
     * Синхронизировать список станций метро яндекса с локальной базой данных
     */
    @Transactional
    public void syncWithDatabase() throws MetroServiceException {
        MetroSystemDto metroSystem = loadStations();

        CityEntity moscowEntity = cityRepository.findByNameAndCountry_Name(MOSCOW, RUSSIA);
        List<MetroEntity> dbStations = metroRepository.findAll();

        metroSystem.getStations().stream().forEach(station -> {
            if (!dbStations.stream().anyMatch(m -> Objects.equals(station.getStation().getName(), m.getStationName()))) {
                MetroEntity entity = new MetroEntity();
                entity.setStationName(station.getStation().getName());
                entity.setCity(moscowEntity);
                entity.setLocation(new GeoPoint(station.getCoords().getLat(), station.getCoords().getLng()));

                metroRepository.saveAndFlush(entity);
            }
        });
    }

    /**
     * Загрузить из интернета список станций метро
     *
     * @throws MetroServiceException
     */
    public MetroSystemDto loadStations() throws MetroServiceException {
        log.debug("Get stations of the metro");

        try {
            MetroStationsDto stationsDto = downloadStationsFromYandex();

            MetroSystemDto metroSystem = new MetroSystemDto();
            metroSystem.setCity(MOSCOW);

            for (String stationId : stationsDto.getStations().keySet()) {
                MetroStationDto stationDto = stationsDto.getStations().get(stationId);
                GoogleStationInfo coords = getStationInfo(stationDto.getName());
                MetroStationFullInfoDto metro = new MetroStationFullInfoDto(coords, stationDto);

                metroSystem.addStation(metro);

                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
            }

            return metroSystem;

        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }

    /**
     * Скачать список станций метро с яндекса
     *
     * @throws bynull.realty.utils.XmlParseException
     * @throws JsonMapperException
     */
    private MetroStationsDto downloadStationsFromYandex() throws bynull.realty.utils.XmlParseException, JsonMapperException {
        log.debug("Download the stations from yandex");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://yastatic.net/metro/2.1.9-8/data/").path("1.ru.svg");
        SvgMetro result = XmlUtils.stringToObject(target.request(MediaType.APPLICATION_SVG_XML_TYPE).get(String.class), SvgMetro.class);

        String stationsJsonString = result.getMetaData().getMetadata();
        return JsonUtils.fromJson(stationsJsonString, MetroStationsDto.class);
    }

    /**
     * Получить координаты станции метро на карте
     * <p>
     * metro savelovskaya: https://maps.googleapis.com/maps/api/place/textsearch/json?query=метро+савеловская&sensor=true&key=AIzaSyD8FthnabTSsiQh9N6QTKGqePNTOEZOhXU
     */
    public GoogleStationInfo getStationInfo(String metroStation) throws MetroServiceException {
        log.debug("Get station info from google: {}", metroStation);

        SslConfigurator sslConfig = SslConfigurator.newInstance();
        SSLContext sslContext = sslConfig.createSSLContext();
        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("https://maps.google.com/maps/api/geocode/json");
        //target = target.queryParam("query", "метро+" + metroStation.replaceAll(" ", "+"));
        target = target.queryParam("address", "город+Москва,+Москва,+метро+" + metroStation.replaceAll(" ", "+"));
        target.queryParam("language", "ru");
        target = target.queryParam("sensor", false);

        String response = target.request(MediaType.TEXT_HTML_TYPE).get(String.class);
        try {
            GoogleStationInfo result = JsonUtils.fromJson(response, GoogleStationInfo.class);
            if (result.getResults().isEmpty()) {
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
