package bynull.realty.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by dionis on 30/01/15.
 */
public class BaseExceptionMapper implements ExceptionMapper<Throwable> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response toResponse(Throwable exception) {
        logger.error("Exception occurred: {}", exception);

        if(exception instanceof WebApplicationException) {
            WebApplicationException e = (WebApplicationException) exception;
            return Response
                    .status(e.getResponse().getStatus())
                    .build();
        } else {
            return Response
                    .status(Response.Status.CONFLICT)
                    .build();
        }
    }
}
