package bynull.realty.web.rest;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.CityDTO;
import bynull.realty.services.api.CityService;
import bynull.realty.web.converters.CityDtoJsonConverter;
import bynull.realty.web.json.CityJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * @author dionis on 22/06/14.
 */
@Component
@Path("cities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CityRestResource {
    @Resource
    CityService cityService;

    @Resource
    CityDtoJsonConverter cityDtoJsonConverter;

    @GET
    @Path("/search")
    public Response findMetroStations(
            @QueryParam("lng") Double lng,
            @QueryParam("lat") Double lat
    ) {

        final Optional<GeoPoint> geoPoint;
        if (lat != null && lng != null) {
            geoPoint = Optional.of(new GeoPoint().withLatitude(lat).withLongitude(lng));
        } else {
            geoPoint = Optional.empty();
        }

        Optional<CityDTO> city = cityService.findByGeoPoint(geoPoint);

        if (city.isPresent()) {
            CityJSON json = cityDtoJsonConverter.toTargetType(city).orElse(null);
            return Response
                    .ok(json)
                    .build();
        } else {
            CityJSON json = cityDtoJsonConverter.toTargetType(cityService.getMoscow()).orElse(null);
            return Response
                    .ok(json)
                    .build();
        }
    }
}
