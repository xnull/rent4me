package bynull.realty.web.rest;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.FacebookPostDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.FacebookScrapingPostService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.web.json.ApartmentJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
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
    FacebookScrapingPostService facebookScrapingPostService;

    @Path("/search")
    @GET
    public Response findPosts(
            @QueryParam("text") String text,
            @QueryParam("limit") int limit,
            @QueryParam("offset") int offset
            ) {

        LimitAndOffset limitAndOffset = LimitAndOffset.builder()
                .withLimit(limit)
                .withOffset(offset)
                .create();

        List<FacebookPostDTO> found = facebookScrapingPostService.findPosts(text, limitAndOffset);

//        List<ApartmentJSON> json = nearest.stream().map(ApartmentJSON::from).collect(Collectors.toList());
        return Response
                .ok(found)
                .build();
    }
}
