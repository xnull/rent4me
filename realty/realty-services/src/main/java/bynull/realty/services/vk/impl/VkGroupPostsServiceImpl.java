package bynull.realty.services.vk.impl;

import bynull.realty.services.vk.VkGroupPostsService;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by trierra on 12/4/14.
 */
@Service
public class VkGroupPostsServiceImpl implements VkGroupPostsService {

    private static final Logger LOG = LoggerFactory.getLogger(VkGroupPostsServiceImpl.class);
    private Map<String, String> groupWallParams = new HashMap<>();
    private Map<String, String> groupConversationParams = new HashMap<>();

    public VkGroupPostsServiceImpl() {
        init();
    }

    @PostConstruct
    public void init() {
        groupWallParams.put("owner_id", "");
        groupWallParams.put("domain", "");
        groupWallParams.put("offset", "1");
        groupWallParams.put("count", "30");
        groupWallParams.put("filter", "");
        groupWallParams.put("extended", "");
        groupWallParams.put("v", "5.27");
    }

    private URI requestBuilder(String methodName, Map<String, String> parameters, String accessToken) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.vk.com/method/" + methodName);
        for (String key : parameters.keySet()) {
            builder.setParameter(key, parameters.get(key));
        }
        builder.setParameter("access_token", accessToken);
        return builder.build();
    }

//    @Override
//    public List<ItemJSON> getWallPostsList(String groupDomain, String accessToken) throws URISyntaxException {
//        LOG.debug("getting posts from '" + groupDomain + "' group");
//
//        groupWallParams.replace("domain", "kvarnado");
//
//        URI uri = requestBuilder("wall.get", groupWallParams, accessToken);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<VkResponseJSON> entity = restTemplate.getForEntity(uri.toString(), VkResponseJSON.class);
//        VkResponseJSON post = entity.getBody();
//        return post.getResponse().getItems();
//    }

    @Override
    public void wallSearch() {

    }

    @Override
    public void wallGetById() {

    }

    @Override
    public void getConversation() {

    }

//    @Override
//    public List<WallPostJSON> getThreadPosts() {
//
//        return null;
//    }

}
