package bynull.realty.web.rest;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.web.json.ApartmentJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dionis on 24/11/14.
 */
@Component
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestResource {
    @Resource
    ApartmentService apartmentService;

    @POST
    @Path("/apartment")
    public Response create(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();
        boolean result = apartmentService.createForAuthorizedPerson(dto);
        return Response
                .status(result ? Response.Status.CREATED : Response.Status.CONFLICT)
                .build();
    }
}
