package bynull.realty.web.rest;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.web.converters.ApartmentDtoJsonConverter;
import bynull.realty.web.json.ApartmentJSON;
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
@Path("apartments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApartmentRestResource {
    @Resource
    ApartmentService apartmentService;

    @Resource
    ApartmentDtoJsonConverter apartmentDtoJsonConverter;

    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") long id) {
        ApartmentDTO dto = apartmentService.find(id);
        if (dto == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            ApartmentJSON json = apartmentDtoJsonConverter.toTargetType(dto);

            return Response
                    .ok(json)
                    .build();
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

        List<ApartmentJSON> json = nearest.stream().map(apartmentDtoJsonConverter::toTargetType).collect(Collectors.toList());
        return Response
                .ok(json)
                .build();
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
            @QueryParam("offset") int offset
    ) {

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        ApartmentRepository.FindMode findMode = ApartmentRepository.FindMode.valueOf(type);

        Set<ApartmentRepository.RoomCount> roomsCount = rooms != null
                ? rooms.stream().map(ApartmentRepository.RoomCount::findByValueOrFail).collect(Collectors.toSet())
                : Collections.emptySet();

        ApartmentRepositoryCustom.GeoParams geoParams = new ApartmentRepositoryCustom.GeoParams();

        if(latLow != null && lngLow != null && latHigh != null && lngHigh != null) {

            geoParams = geoParams.withBoundingBox(Optional.of(
                    new ApartmentRepositoryCustom.BoundingBox()
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
            geoParams = geoParams.withPoint(Optional.of(new GeoPoint().withLatitude(lat).withLongitude(lng)));
        } else {
            geoParams = geoParams.withPoint(Optional.empty());
        }



        List<ApartmentDTO> found = apartmentService.findPosts(text, withSubway, roomsCount, minPrice, maxPrice, findMode, geoParams, metroIds, limitAndOffset);
        List<ApartmentJSON> result = found.stream().map(apartmentDtoJsonConverter::toTargetType).collect(Collectors.toList());

        return Response
                .ok(result)
                .build();
    }
}
