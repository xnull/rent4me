package bynull.realty.crawler;

import bynull.realty.crawler.api.VkApiGroups;
import bynull.realty.crawler.json.ThreadPost;
import bynull.realty.crawler.json.WallPost;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by trierra on 12/4/14.
 */
@Component
public class VkApiGroupsImpl implements VkApiGroups {
    private Map<String, String> groupWallParams = new HashMap<>();
    private Map<String, String> groupConversationParams = new HashMap<>();

    public VkApiGroupsImpl() {
        init();
    }

    @PostConstruct
    private void init() {
        groupWallParams.put("owner_id", "");
        groupWallParams.put("domain", "");
        groupWallParams.put("offset", "1");
        groupWallParams.put("count", "30");
        groupWallParams.put("filter", "");
        groupWallParams.put("extended", "");
        groupWallParams.put("version", "5.27");
    }

    private URI requestBuilder(String methodName, Map<String, String> parameters, String accessToken) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.vk.com/method/" + methodName);
        for (String key : parameters.keySet()) {
            builder.setParameter(key, parameters.get(key));
        }
        builder.setParameter("access_token", accessToken);
        return builder.build();
//        return "https://api.vk.com/method/users.get?user_id=66748&v=5.27&access_token=533bacf01e11f55b536a565b57531ac114461ae8736d6506a3";
    }

    @Override
    public List<WallPost> wallGetPostsList(String accessToken) throws URISyntaxException {
        groupWallParams.replace("domain", "kvarnado");
        URI uri = requestBuilder("wall.get", groupWallParams, accessToken);

        return null;
    }

    @Override
    public void wallSearch() {

    }

    @Override
    public void wallGetById() {

    }

    @Override
    public void getConversation() {

    }

    @Override
    public List<ThreadPost> getThreadPosts() {

        return null;
    }

//    arenda_v_moskve
//    club22062158
//    sdalsnyal
//    arendakvartir_ru


}
