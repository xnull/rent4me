package bynull.realty.grabber.services;

import bynull.realty.grabber.json.ItemJSON;
import bynull.realty.grabber.json.VkResponseJSON;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by trierra on 12/4/14.
 */
@Service
public class VkPostsServiceImpl implements VkPostsService {

    private static final Logger LOG = LoggerFactory.getLogger(VkPostsServiceImpl.class);
    private Map<String, String> groupWallParams = new HashMap<>();
    private Map<String, String> groupTopicParams = new HashMap<>();

    public VkPostsServiceImpl() {
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

        groupTopicParams.put("group_id", "");
        groupTopicParams.put("topic_id", "");
        groupTopicParams.put("need_likes", "");
        groupTopicParams.put("offset", "1");
        groupTopicParams.put("count", "30");
        groupTopicParams.put("extended", "");
        groupTopicParams.put("sort", "desc");
        groupTopicParams.put("v", "5.28");

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

    @Override
    public List<ItemJSON> getWallPostsList(String groupDomain, String accessToken) throws URISyntaxException {
        LOG.debug("getting posts from '" + groupDomain + "' group");
        groupWallParams.replace("domain", groupDomain);
        return getPosts("wall.get", accessToken, groupWallParams);
    }

    @Override
    public List<ItemJSON> getThreadPostsList(String topicId, String groupId, String accessToken) throws URISyntaxException {
        LOG.debug("getting comments from group: '" + groupId + "', topic: '" + topicId + "'");

        groupTopicParams.replace("group_id", groupId);
        groupTopicParams.replace("topic_id", topicId);

        return getPosts("board.getComments", accessToken, groupTopicParams);
    }

    private List<ItemJSON> getPosts(String method, String accessToken, Map params) throws URISyntaxException {
        URI uri = requestBuilder(method, params, accessToken);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VkResponseJSON> entity = restTemplate.getForEntity(uri.toString(), VkResponseJSON.class);
        VkResponseJSON post = entity.getBody();
        return post.getResponse().getItems();
    }

    @Override
    public void wallSearch() {
        throw new NotImplementedException();
    }

}
