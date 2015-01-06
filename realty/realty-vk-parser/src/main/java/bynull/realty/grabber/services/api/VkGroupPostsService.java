package bynull.realty.grabber.services.api;

import bynull.realty.grabber.json.Item;
import bynull.realty.grabber.json.WallPost;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/4/14.
 */
public interface VkGroupPostsService {

    List<Item> getWallPostsList(String groupDomain, String accessToken) throws URISyntaxException;

    void wallSearch();

    void wallGetById();

    void getConversation();

    List<WallPost> getThreadPosts();
}
