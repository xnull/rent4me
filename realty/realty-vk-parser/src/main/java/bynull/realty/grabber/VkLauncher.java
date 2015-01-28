package bynull.realty.grabber;

import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.json.ItemJSON;
import bynull.realty.grabber.services.VkPostsService;
import bynull.realty.services.vk.VkDataStoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trierra on 12/9/14.
 */

@Service
public class VkLauncher {

    @Resource
    private VkPostsService vkPostsService;

    @Resource
    private VkDataStoreService vkDataStoreService;

    @Resource
    private VkAuth vkAuth;

    private String accessToken;

    public void launch() throws EmptyHiddenVkValue, IOException, URISyntaxException {
        accessToken = vkAuth.receiveToken();
        vkDataStoreService.savePosts(getWallPosts("club22062158"));
        vkDataStoreService.savePosts(getWallPosts("kvarnado"));
        vkDataStoreService.savePosts(getWallPosts("sdalsnyal"));

//        https://vk.com/topic-854065_29786073
        vkDataStoreService.savePosts(getThreadPosts("29786073", "854065"));
        //https://vk.com/topic-854065_29786078
        vkDataStoreService.savePosts(getThreadPosts("29786078", "854065"));
//        https://vk.com/topic-854065_29786102
        vkDataStoreService.savePosts(getThreadPosts("29786102", "854065"));
        //https://vk.com/topic-13185834_30591101
        vkDataStoreService.savePosts(getThreadPosts("30591101", "13185834"));
        //https://vk.com/topic-13185834_30591200
        vkDataStoreService.savePosts(getThreadPosts("30591200", "13185834"));
    }

    private List<ItemDTO> getThreadPosts(String topicId, String threadId) throws URISyntaxException {
        return vkPostsService.getThreadPostsList(topicId, threadId, accessToken).stream().map(ItemJSON::toDto).collect(Collectors.toList());
    }

    public List<ItemDTO> getWallPosts(String groupId) throws EmptyHiddenVkValue, IOException, URISyntaxException {
        return vkPostsService.getWallPostsList(groupId, accessToken).stream().map(ItemJSON::toDto).collect(Collectors.toList());
    }

}
