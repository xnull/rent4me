package bynull.realty.services.impl;

import bynull.realty.common.JsonUtils;
import bynull.realty.common.JsonUtils.JsonMapperException;
import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.MetroDTO;
import bynull.realty.services.api.MetroService;
import bynull.realty.services.metro.*;
import bynull.realty.services.metro.MetroSystemDto.MetroStationFullInfoDto;
import bynull.realty.utils.XmlUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.SslConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static bynull.realty.services.metro.MetroStationsDto.MetroStationDto;

/**
 * @author Vyacheslav Petc
 * @since 10.08.14.
 */
@Service
@Lazy
@Slf4j
public class MetroServiceImpl implements MetroService {
    private static final String MOSCOW = "Москва";
    private static final String RUSSIA = "Россия";
    private static final String ST_PETERSBURG = "Санкт-Петербург";

    public static class CityDescription {
        private final String country;
        private final String city;
        private final GeoPoint cityCenterPoint;

        public CityDescription(String country, String city, GeoPoint cityCenterPoint) {
            this.country = country;
            this.city = city;
            this.cityCenterPoint = cityCenterPoint;
        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public GeoPoint getCityCenterPoint() {
            return cityCenterPoint;
        }
    }

    @VisibleForTesting
    //POINT(37.63249495 55.749792)
    public static final CityDescription MOSCOW_CITY_DESCRIPTION = new CityDescription(RUSSIA, MOSCOW, new GeoPoint().withLongitude(37.63249495).withLatitude(55.749792));
    //POINT(30.3250575 59.91744545)
    private static final CityDescription ST_PETERSBURG_CITY_DESCRIPTION = new CityDescription(RUSSIA, ST_PETERSBURG, new GeoPoint().withLongitude(30.3250575).withLatitude(59.91744545));

    private static Client REST_CLIENT = ClientBuilder.newBuilder().build();

    @Autowired
    private MetroRepository metroRepository;
    @Autowired
    private CityRepository cityRepository;

    @Resource
    MetroModelDTOConverter metroModelDTOConverter;

    @Transactional
    @Override
    public List<? extends MetroDTO> findMetros(ApartmentRepositoryCustom.GeoParams geoParams) {
        List<? extends MetroDTO> metroDTOs = metroModelDTOConverter.toTargetList(metroRepository.findMetros(geoParams));
        return metroDTOs;
    }

    /**
     * Синхронизировать список станций метро яндекса с локальной базой данных
     */
    @Transactional
    @Override
    public void syncMoscowMetrosWithDatabase() throws MetroServiceException {
        syncMetros(MOSCOW_CITY_DESCRIPTION);
    }

    @Transactional
    @Override
    public void syncStPetersburgMetrosWithDatabase() throws MetroServiceException {
        syncMetros(ST_PETERSBURG_CITY_DESCRIPTION);
    }

    private void syncMetros(CityDescription cityDescription) throws MetroServiceException {

        MetroSystemDto metroSystem = loadStations(cityDescription);

        CityEntity city = cityRepository.findByPoint(cityDescription.cityCenterPoint.getLongitude(), cityDescription.cityCenterPoint.getLatitude());
        Assert.notNull(city, "City not found by provided geopoint " + cityDescription.cityCenterPoint);
        List<MetroEntity> dbStations = metroRepository.findAll()
                .stream()
                        //remove metros from cities other than provided
                .filter(metro -> {
                    final boolean allowed;
                    //TODO What to do if city is null?
                    BoundingBox area = metro.getCity().get().getArea();
                    allowed = area.contains(cityDescription.cityCenterPoint);

                    return allowed;
                })
                .collect(Collectors.toList());

        Set<String> stationNamesSavedInThisSync = new HashSet<>();

        metroSystem.getStations()
                .forEach(station -> {
                    if (!dbStations.stream()
                            .anyMatch(m -> stationNamesSavedInThisSync.contains(station.getStation().getName())
                                    || Objects.equals(station.getStation().getName(), m.getStationName()))) {
                        MetroEntity entity = new MetroEntity();
                        entity.setStationName(station.getStation().getName());
                        entity.setCity(city);
                        entity.setLocation(new GeoPoint()
                                        .withLatitude(station.getCoords().getLat())
                                        .withLongitude(station.getCoords().getLng())
                        );


                        stationNamesSavedInThisSync.add(station.getStation().getName());

                        metroRepository.saveAndFlush(entity);
                    }
                });
    }

    /**
     * Загрузить из интернета список станций метро
     *
     * @throws MetroServiceException
     */
    public MetroSystemDto loadStations(CityDescription cityDescription) throws MetroServiceException {
        log.debug("Get stations of the metro");

        try {
            MetroStationsDto stationsDto = downloadStationsFromYandex(cityDescription);

            MetroSystemDto metroSystem = new MetroSystemDto();
            metroSystem.setCity(cityDescription.city);

            for (String stationId : stationsDto.getStations().keySet()) {
                MetroStationDto stationDto = stationsDto.getStations().get(stationId);
                GoogleStationInfo coords = getStationInfo(cityDescription, stationDto.getName());
                MetroStationFullInfoDto metro = new MetroStationFullInfoDto(coords, stationDto);

                metroSystem.addStation(metro);

                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
            }

            return metroSystem;

        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }


    private static final Map<String, String> COUNTRY_AND_CITY_TO_URL = ImmutableMap.of(
            generateKeyForCountryAndCityUrl(RUSSIA, MOSCOW), "http://yastatic.net/metro/2.1.9-8/data/1.ru.svg",
            generateKeyForCountryAndCityUrl(RUSSIA, ST_PETERSBURG), "http://yastatic.net/metro/2.1.9-8/data/2.ru.svg"
    );

    private static String generateKeyForCountryAndCityUrl(String country, String city) {
        return country + "_" + city;
    }

    /**
     * Скачать список станций метро с яндекса
     *
     * @throws bynull.realty.utils.XmlParseException
     * @throws JsonMapperException
     */
    private MetroStationsDto downloadStationsFromYandex(CityDescription cityDescription) throws bynull.realty.utils.XmlParseException, JsonMapperException {
        log.debug("Download the stations from yandex");

        String url = COUNTRY_AND_CITY_TO_URL.get(generateKeyForCountryAndCityUrl(cityDescription.country, cityDescription.city));

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        SvgMetro result = XmlUtils.stringToObject(target.request(MediaType.APPLICATION_SVG_XML_TYPE).get(String.class), SvgMetro.class);

        String stationsJsonString = result.getMetaData().getMetadata();
        return JsonUtils.fromJson(stationsJsonString, MetroStationsDto.class);
    }

    /**
     * Получить координаты станции метро на карте
     * <p>
     * metro savelovskaya: https://maps.googleapis.com/maps/api/place/textsearch/json?query=метро+савеловская&sensor=true&key=AIzaSyD8FthnabTSsiQh9N6QTKGqePNTOEZOhXU
     */
    public GoogleStationInfo getStationInfo(CityDescription cityDescription, String metroStation) throws MetroServiceException {
        log.debug("Get station info from google: {}", metroStation);

        SslConfigurator sslConfig = SslConfigurator.newInstance();
        SSLContext sslContext = sslConfig.createSSLContext();
        WebTarget target = REST_CLIENT.target("https://maps.google.com/maps/api/geocode/json");
        //target = target.queryParam("query", "метро+" + metroStation.replaceAll(" ", "+"));
        target = target.queryParam("address", "город+" + cityDescription.city + ",+" + cityDescription.city + ",+метро+" + metroStation.replaceAll(" ", "+"));
        target.queryParam("language", "ru");
        target = target.queryParam("sensor", false);

        String response = target.request(MediaType.TEXT_HTML_TYPE).get(String.class);
        try {
            GoogleStationInfo result = JsonUtils.fromJson(response, GoogleStationInfo.class);
            if (result.getResults().isEmpty()) {
                String errorStr = mapToErrorDto(response).getErrorMessage();
                if (errorStr == null) {
                    errorStr = response;
                }
                throw new MetroServiceException("Error loading data: " + errorStr);
            }
            return result;
        } catch (JsonMapperException e) {
            throw new MetroServiceException(e);
        }

    }

    private GoogleApiErrorDto mapToErrorDto(String json) throws JsonMapperException {
        return JsonUtils.fromJson(json, GoogleApiErrorDto.class);
    }

    public MetroStationsDto getStationsFromYandex(CityDescription cityDescription) throws MetroServiceException {
        try {
            return downloadStationsFromYandex(cityDescription);
        } catch (Exception e) {
            throw new MetroServiceException(e);
        }
    }
}
