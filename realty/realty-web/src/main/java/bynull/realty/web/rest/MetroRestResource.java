package bynull.realty.web.rest;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.MetroDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.MetroService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.web.converters.ApartmentDtoJsonConverter;
import bynull.realty.web.converters.MetroDtoJsonConverter;
import bynull.realty.web.json.ApartmentJSON;
import bynull.realty.web.json.MetroJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dionis on 22/06/14.
 */
@Component
@Path("metros")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetroRestResource {
    @Resource
    MetroService metroService;

    @Resource
    MetroDtoJsonConverter metroDtoJsonConverter;

    @GET
    @Path("/search")
    public Response findMetroStations(
            @QueryParam("lng") Double lng,
            @QueryParam("lat") Double lat,
            @QueryParam("country_code") String countryCode,
            //optional bounding box params
            //lat_lo="+sw.lat()+"&lng_lo="+sw.lng()+"&lat_hi="+ne.lat()+"&lng_hi="+ne.lng()
            @QueryParam("lat_lo") Double latLow,
            @QueryParam("lng_lo") Double lngLow,
            @QueryParam("lat_hi") Double latHigh,
            @QueryParam("lng_hi") Double lngHigh
    ) {


        boolean someDataSpecified = false;
        ApartmentRepositoryCustom.GeoParams geoParams = new ApartmentRepositoryCustom.GeoParams();

        if(latLow != null && lngLow != null && latHigh != null && lngHigh != null) {
            someDataSpecified = true;
            geoParams = geoParams.withBoundingBox(Optional.of(
                    new BoundingBox()
                            .withLow(
                                    new GeoPoint()
                                            .withLatitude(latLow)
                                            .withLongitude(lngLow)
                            )
                            .withHigh(
                                    new GeoPoint()
                                            .withLatitude(latHigh)
                                            .withLongitude(lngHigh)
                            )
            ));
        } else {
            geoParams = geoParams.withBoundingBox(Optional.empty());
        }

        geoParams = geoParams.withCountryCode(Optional.ofNullable(countryCode));

        if (lat != null && lng != null) {
            someDataSpecified = true;
            geoParams = geoParams.withPoint(Optional.of(new GeoPoint().withLatitude(lat).withLongitude(lng)));
        } else {
            geoParams = geoParams.withPoint(Optional.empty());
        }

        List<? extends MetroDTO> metros = someDataSpecified ? metroService.findMetros(geoParams) : metroService.findMoscowMetros();
        List<? extends MetroJSON> json = metroDtoJsonConverter.toTargetList(metros);

        return Response
                .ok(json)
                .build();
    }
}
