package bynull.realty.web.rest;

import bynull.realty.dao.apartment.ApartmentRepository;
import bynull.realty.dao.apartment.ApartmentRepository.FindMode;
import bynull.realty.dao.apartment.ApartmentRepositoryCustom.GeoParams;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.web.converters.ApartmentDtoJsonConverter;
import bynull.realty.web.json.ApartmentJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dionis on 22/06/14.
 */
@Slf4j
@Component
@Path("apartments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApartmentRestResource {
    private final Map<String, String> templateCache = new HashMap<>();

    @Resource
    ApartmentService apartmentService;

    @Resource
    ApartmentDtoJsonConverter apartmentDtoJsonConverter;

    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") long id, @HeaderParam("user-agent") String userAgent) {
        log.info("User agent provided: [{}]", userAgent);
        Optional<ApartmentDTO> dto = apartmentService.find(id);
        if (!dto.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Optional<ApartmentJSON> json = apartmentDtoJsonConverter.toTargetType(dto);
            return Response.ok(json.orElse(null)).build();
        }
    }

    @POST
    public Response create(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentDtoJsonConverter.toSourceType(apartmentJSON);
        apartmentService.create(dto);
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @Path("/nearest")
    @GET
    public Response findNearest(
            @QueryParam("lng") double lng,
            @QueryParam("lat") double lat,
            @QueryParam("country_code") String countryCode,
            //optional bounding box params
            //lat_lo="+sw.lat()+"&lng_lo="+sw.lng()+"&lat_hi="+ne.lat()+"&lng_hi="+ne.lng()
            @QueryParam("lat_lo") Double latLow,
            @QueryParam("lng_lo") Double lngLow,
            @QueryParam("lat_hi") Double latHigh,
            @QueryParam("lng_hi") Double lngHigh,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ) {

        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLongitude(lng);
        geoPoint.setLatitude(lat);

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        List<ApartmentDTO> nearest = apartmentService.findNearestForCountry(geoPoint, countryCode, latLow, lngLow, latHigh, lngHigh, limitAndOffset);

        List<ApartmentJSON> json = apartmentDtoJsonConverter.toTargetListOf(nearest);
        return Response.ok(json).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentDtoJsonConverter.toSourceType(apartmentJSON);
        apartmentService.update(dto);
        return Response
                .ok()
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOne(@PathParam("id") long id) {
        apartmentService.delete(id);

        return Response
                .ok()
                .build();

    }

    @Path("/search")
    @GET
    public Response findPosts(
            @QueryParam("text") String text,
            @QueryParam("type") String type,
            @QueryParam("with_subway") boolean withSubway,
            @QueryParam("rooms") List<String> rooms,
            @QueryParam("min_price") Integer minPrice,
            @QueryParam("max_price") Integer maxPrice,
            @QueryParam("lng") Double lng,
            @QueryParam("lat") Double lat,
            @QueryParam("country_code") String countryCode,
            //optional bounding box params
            //lat_lo="+sw.lat()+"&lng_lo="+sw.lng()+"&lat_hi="+ne.lat()+"&lng_hi="+ne.lng()
            @QueryParam("lat_lo") Double latLow,
            @QueryParam("lng_lo") Double lngLow,
            @QueryParam("lat_hi") Double latHigh,
            @QueryParam("lng_hi") Double lngHigh,
            @QueryParam("metro_ids") List<Long> metroIds,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset) {

        log.trace("Search apartments");

        LimitAndOffset limitAndOffset = LimitAndOffset.builder().withLimit(limit).withOffset(offset).create();

        FindMode findMode = FindMode.valueOf(type);

        Set<ApartmentRepository.RoomCount> roomsCount = rooms != null
                ? rooms.stream().map(ApartmentRepository.RoomCount::findByValueOrFail).collect(Collectors.toSet())
                : Collections.emptySet();

        GeoParams geoParams = new GeoParams();

        if (latLow != null && lngLow != null && latHigh != null && lngHigh != null) {

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

        if (lat != null && lng != null && !geoParams.getBoundingBox().isPresent()) {
            geoParams = geoParams.withPoint(Optional.of(new GeoPoint().withLatitude(lat).withLongitude(lng)));
        } else {
            geoParams = geoParams.withPoint(Optional.empty());
        }

        List<ApartmentDTO> found = apartmentService.findPosts(text, withSubway, roomsCount, minPrice, maxPrice, findMode, geoParams, metroIds, limitAndOffset);
        List<ApartmentJSON> result = apartmentDtoJsonConverter.toTargetListOf(found);

        return Response.ok(result).build();
    }

    @Path("/{id}/similar")
    @GET
    public Response findSimilar(
            @PathParam("id") long apartmentId
    ) {


        List<? extends ApartmentDTO> found = apartmentService.findSimilarToApartment(apartmentId);

        List<? extends ApartmentJSON> result = apartmentDtoJsonConverter.toTargetList(found);

        return Response
                .ok(result)
                .build();
    }
}
