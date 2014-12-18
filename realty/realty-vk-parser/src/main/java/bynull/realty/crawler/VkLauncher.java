package bynull.realty.crawler;

import bynull.realty.crawler.api.VkApiGroups;
import bynull.realty.crawler.json.ThreadPost;
import bynull.realty.crawler.json.WallPost;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/9/14.
 */

@Component
@Path("vk")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VkLauncher {

    @Autowired
    private VkApiGroups vkApiGroups;
    private String accessToken;

    @GET
    @Path("/{groupId}")
    public Response launch(@PathParam("groupId") String groupId) throws EmptyHiddenVkValue, IOException, URISyntaxException {
        VkAuth vkAuth = new VkAuth();
        accessToken = vkAuth.receiveToken();
        return null;
    }

    public List<WallPost> getWallPostsList() throws URISyntaxException {
        return vkApiGroups.wallGetPostsList(accessToken);
    }

    public List<ThreadPost> getThreadPostsList(){
        return vkApiGroups.getThreadPosts();
    }

    private void getPostDiffList(){

    }
}
