package bynull.realty.crawler.api;

import bynull.realty.crawler.json.Item;
import bynull.realty.crawler.json.WallPost;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/4/14.
 */
public interface VkApiGroups {
    List<Item> wallGetPostsList(String groupDomain, String accessToken) throws URISyntaxException;
    void wallSearch();
    void wallGetById();
    void getConversation();

    List<WallPost> getThreadPosts();
}
