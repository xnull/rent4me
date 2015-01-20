package bynull.realty.web.rest;

import bynull.realty.services.metro.MetroServiceException;
import bynull.realty.services.metro.MoscowMetroSynchronisationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Рест сервис для админки (временный)
 *
 * @author Vyacheslav Petc
 * @since 1/9/15.
 */
@Component
@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class AdminRestResource {

    @Resource
    private MoscowMetroSynchronisationService moscowMetroSynchronisationService;

    @GET
    @Path("syncmetro")
    public Response listAll() throws MetroServiceException {
        try {
            moscowMetroSynchronisationService.syncWithDatabase();
        } catch (MetroServiceException e) {
            log.error("Error sync", e);
            throw e;
        }
        return Response.ok("Success").build();
    }
}