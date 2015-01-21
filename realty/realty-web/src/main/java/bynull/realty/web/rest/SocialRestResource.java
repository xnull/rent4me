package bynull.realty.web.rest;

import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.services.api.FacebookService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.web.converters.FacebookPostDtoJsonConverter;
import bynull.realty.web.json.FacebookPostJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dionis on 22/06/14.
 */
@Component
@Path("social")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SocialRestResource {
    @Resource
    FacebookService facebookService;

    @Resource
    FacebookPostDtoJsonConverter facebookPostConverter;

    @Path("/renter/search")
    @GET
    public Response findRenterPosts(
            @QueryParam("text") String text,
            @QueryParam("with_subway") boolean withSubway,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ) {

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        List<FacebookPostDTO> found = facebookService.findRenterPosts(text, withSubway, limitAndOffset);
        List<FacebookPostJSON> result = facebookPostConverter.toTargetList(found);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(result)
                .build();
    }

    @Path("/fb/search")
    @GET
    public Response findFBPosts(
            @QueryParam("text") String text,
            @QueryParam("type") String type,
            @QueryParam("with_subway") boolean withSubway,
            @QueryParam("rooms") List<String> rooms,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ) {

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        FacebookService.FindMode findMode = FacebookService.FindMode.valueOf(type);

        Set<FacebookService.RoomCount> roomsCount = rooms != null
                ? rooms.stream().map(FacebookService.RoomCount::findByValueOrFail).collect(Collectors.toSet())
                : Collections.emptySet();

        List<FacebookPostDTO> found = facebookService.findFBPosts(text, withSubway, roomsCount, limitAndOffset, findMode);
        List<FacebookPostJSON> result = facebookPostConverter.toTargetList(found);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(result)
                .build();
    }

    @Path("/lessor/search")
    @GET
    public Response findLessorPosts(
            @QueryParam("text") String text,
            @QueryParam("with_subway") boolean withSubway,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
    ) {

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        List<FacebookPostDTO> found = facebookService.findLessorPosts(text, withSubway, limitAndOffset);
        List<FacebookPostJSON> result = facebookPostConverter.toTargetList(found);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(result)
                .build();
    }
}
