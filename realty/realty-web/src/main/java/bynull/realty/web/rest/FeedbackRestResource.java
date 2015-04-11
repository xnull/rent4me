package bynull.realty.web.rest;

import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.MetroDTO;
import bynull.realty.services.api.FeedbackService;
import bynull.realty.services.api.MetroService;
import bynull.realty.web.converters.FeedbackDtoJsonConverter;
import bynull.realty.web.converters.MetroDtoJsonConverter;
import bynull.realty.web.json.FeedbackJSON;
import bynull.realty.web.json.MetroJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dionis on 22/06/14.
 */
@Component
@Path("feedback")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackRestResource {
    @Resource
    FeedbackService feedbackService;

    @Resource
    FeedbackDtoJsonConverter feedbackDtoJsonConverter;

    @POST
    public Response findMetroStations(
            FeedbackJSON feedbackJSON
    ) {
        FeedbackDTO feedbackDTO = feedbackDtoJsonConverter.toSourceType(feedbackJSON);

        feedbackService.createFeedback(feedbackDTO);

        return Response
                .status(Response.Status.CREATED)
                .build();
    }
}
