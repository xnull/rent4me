package bynull.realty.grabber;

import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.json.Item;
import bynull.realty.grabber.json.WallPost;
import bynull.realty.grabber.services.api.VkDataStoreService;
import bynull.realty.grabber.services.api.VkGroupPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/9/14.
 */

@Service
public class VkLauncher {

    @Resource
    private VkGroupPostsService vkGroupPostsService;

    @Resource
    private VkDataStoreService vkDataStoreService;

    @Autowired
    private VkAuth vkAuth;

    private String accessToken;

    @Scheduled
    public void launch() throws EmptyHiddenVkValue, IOException, URISyntaxException {
//        vkDataStoreService.savePosts(convertToPojo(club22062158));

        List<Item> kvarnado = vkGroupPostsService.getWallPostsList("kvarnado", accessToken);
        List<Item> sdalsnyal = vkGroupPostsService.getWallPostsList("sdalsnyal", accessToken);

    }

    public List<ItemDTO> getWallPosts(String groupId) throws EmptyHiddenVkValue, IOException, URISyntaxException {
        accessToken = vkAuth.receiveToken();
        return null;
//        return convertToPojo(vkGroupPostsService.getWallPostsList(groupId, accessToken));
    }

    public void saveWallPost(List<ItemDTO> posts) {
        vkDataStoreService.savePosts(posts);
    }

    private List<WallPost> getPostDiffList() {
        return null;
    }

    public void setVkGroupPostsService(VkGroupPostsService vkGroupPostsService) {
        this.vkGroupPostsService = vkGroupPostsService;
    }

    public void setVkDataStore(VkDataStoreService vkDataStoreService) {
        this.vkDataStoreService = vkDataStoreService;
    }

    public void setVkAuth(VkAuth vkAuth) {
        this.vkAuth = vkAuth;
    }
}
