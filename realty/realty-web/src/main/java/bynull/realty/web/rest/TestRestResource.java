package bynull.realty.web.rest;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Rest resource is needed for test
 * Created by null on 18.06.14.
 */
@Component
@Path("test")
public class TestRestResource {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response test(){
        return Response.ok("{code: \"ok\"}").build();
    }
}
