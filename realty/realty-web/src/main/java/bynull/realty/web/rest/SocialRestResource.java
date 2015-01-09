package bynull.realty.web.rest;

import bynull.realty.dto.FacebookPostDTO;
import bynull.realty.services.api.FacebookScrapingPostService;
import bynull.realty.util.LimitAndOffset;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author dionis on 22/06/14.
 */
@Component
@Path("social")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SocialRestResource {
    @Resource
    FacebookScrapingPostService facebookScrapingPostService;

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

        List<FacebookPostDTO> found = facebookScrapingPostService.findRenterPosts(text, withSubway, limitAndOffset);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(found)
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

        List<FacebookPostDTO> found = facebookScrapingPostService.findLessorPosts(text, withSubway, limitAndOffset);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(found)
                .build();
    }
}
