package bynull.realty.grabber.services;

import bynull.realty.grabber.json.ItemJSON;
import bynull.realty.grabber.json.WallPostJSON;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/4/14.
 */
public interface VkPostsService {

    List<ItemJSON> getWallPostsList(String groupDomain, String accessToken) throws URISyntaxException;

    void wallSearch();

    void wallGetById();

    void getConversation();

    List<WallPostJSON> getThreadPosts();
}
