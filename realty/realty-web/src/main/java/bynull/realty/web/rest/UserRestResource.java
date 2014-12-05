package bynull.realty.web.rest;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.UserService;
import bynull.realty.web.json.ApartmentJSON;
import bynull.realty.web.json.UserJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Resource
    UserService userService;

    @GET
    @Path("/me")
    public Response getMyProfile() {
        UserDTO user = userService.getMyProfile();

        return Response
                .status(user != null ? Response.Status.OK : Response.Status.NOT_FOUND)
                .entity(UserJSON.from(user))
                .build();
    }

    @PUT
    @Path("/me")
    public Response updateMyProfile(UserJSON userJSON) {
        UserDTO dto = userJSON.toDTO();
        boolean result = userService.updateMyProfile(dto);
        return Response
                .status(result ? Response.Status.OK : Response.Status.CONFLICT)
//                .entity(result ? userJSON.findAuthorizedUserApartment() : null)
                .build();
    }

    @GET
    @Path("/apartment")
    public Response getApartment() {

        ApartmentDTO apartment = apartmentService.findAuthorizedUserApartment();
        return Response
                .status(apartment != null ? Response.Status.OK : Response.Status.NOT_FOUND)
                .entity(ApartmentJSON.from(apartment))
                .build();
    }

    @POST
    @Path("/apartment")
    public Response createApartment(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();
        boolean result = apartmentService.createForAuthorizedUser(dto);
        return Response
                .status(result ? Response.Status.CREATED : Response.Status.CONFLICT)
                .entity(result ? ApartmentJSON.from(apartmentService.findAuthorizedUserApartment()) : null)
                .build();
    }

    @DELETE
    @Path("/apartment")
    public Response deleteApartment() {
        apartmentService.deleteApartmentForAuthorizedUser();
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
