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
 * @author dionis on 22/06/14.
 */
@Component
@Path("apartments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApartmentRestResource {
    @Resource
    ApartmentService apartmentService;

    @GET
    public Response listAll() {
        List<ApartmentDTO> all = apartmentService.findAll();
        List<ApartmentJSON> jsonList = new ArrayList<ApartmentJSON>(all.size());
        for (ApartmentDTO apartmentDTO : all) {
            jsonList.add(ApartmentJSON.from(apartmentDTO));
        }
        return Response
                .ok(jsonList)
                .build();
    }

    @GET
    @Path("/{id}")
    public Response findOne(@PathParam("id") long id) {
        ApartmentDTO dto = apartmentService.find(id);
        if (dto == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        } else {
            ApartmentJSON json = ApartmentJSON.from(dto);

            return Response
                    .ok(json)
                    .build();
        }
    }

    @POST
    public Response create(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();
        apartmentService.create(dto);
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();
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
}
