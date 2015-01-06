package bynull.realty.web.rest;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.ChatService;
import bynull.realty.services.api.UserService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.SecurityUtils;
import bynull.realty.web.annotation.PATCH;
import bynull.realty.web.json.ApartmentJSON;
import bynull.realty.web.json.ChatMessageJSON;
import bynull.realty.web.json.UserJSON;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dionis on 24/11/14.
 */
@Component
@Path("users")
@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
@Produces(MediaType.APPLICATION_JSON)
public class UserRestResource {
    @Resource
    ApartmentService apartmentService;

    @Resource
    ApartmentPhotoService apartmentPhotoService;

    @Resource
    UserService userService;

    @Resource
    ChatService chatService;

    @GET
    @Path("/find")
    public Response findUsersByName(@QueryParam("name") String name) {
        List<UserDTO> users = userService.findByName(name);

        return Response
                .ok()
                .entity(users.stream().map(UserJSON::from).collect(Collectors.toList()))
                .build();
    }

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
                .entity(result ? UserJSON.from(userService.getMyProfile()) : null)
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

    @PATCH
    @Path("/apartment")
    public Response updateApartmentRentInfo(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();
        boolean result = apartmentService.updateForAuthorizedUser(dto);
        return Response
                .status(result ? Response.Status.OK : Response.Status.CONFLICT)
                .entity(result ? ApartmentJSON.from(apartmentService.findAuthorizedUserApartment()) : null)
                .build();
    }

    @POST
    @Path("/apartment/data_change_request")
    public Response createApartmentDataChangeRequest(ApartmentJSON apartmentJSON) {
        ApartmentDTO dto = apartmentJSON.toDTO();

        apartmentService.requestApartmentInfoChangeForAuthorizedUser(dto);

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @POST
    @Path("/apartment/pictures/temp")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPictures(FormDataMultiPart form) {
        FormDataBodyPart filePart = form.getField("file");
//        ContentDisposition contentDisposition = filePart.getContentDisposition();
        InputStream inputStream = filePart.getValueAs(InputStream.class);
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            String photoTempGUID = apartmentPhotoService.createPhotoTemp(content);
            HashMap<String, String> entity = new HashMap<>();
            entity.put("guid", photoTempGUID);
            return Response
                    .status(Response.Status.OK)
                    .entity(entity)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @Path("/apartment")
    public Response deleteApartment() {
        apartmentService.deleteApartmentForAuthorizedUser();
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @POST
    @Path("/{id}/chats")
    public Response createChatMessage(@PathParam("id") long id, ChatMessageJSON message) {
        String content = message.getMessage();
        ChatMessageDTO result = chatService.createChatMessage(id, content);
        return Response
                .status(Response.Status.CREATED)
                .entity(ChatMessageJSON.from(result))
                .build();
    }

    @GET
    @Path("/me/chats")
    public Response listMyChats() {
        List<ChatMessageDTO> result = chatService.listMyLatestChatMessages();
        return Response
                .ok()
                .entity(result.stream().map(ChatMessageJSON::from).collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/me/chats/{chatKey}")
    public Response listMyConversation(@PathParam("chatKey") String chatKey) {
        List<ChatMessageDTO> result = chatService.listMyLatestChatMessagesByKey(new ChatMessageDTO.ChatKeyDTO(chatKey), LimitAndOffset.builder().withLimit(100).create());
        return Response
                .ok()
                .entity(result.stream().map(ChatMessageJSON::from).collect(Collectors.toList()))
                .build();
    }

}
