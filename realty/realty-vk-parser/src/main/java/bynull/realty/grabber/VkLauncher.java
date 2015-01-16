package bynull.realty.grabber;

import bynull.realty.dto.vk.ItemDTO;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.json.ItemJSON;
import bynull.realty.grabber.json.WallPostJSON;
import bynull.realty.grabber.services.VkPostsService;
import bynull.realty.services.vk.VkDataStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @Autowired
    private VkAuth vkAuth;

    private String accessToken;

    @PostConstruct
    public void launch() throws EmptyHiddenVkValue, IOException, URISyntaxException {
        accessToken = vkAuth.receiveToken();
        vkDataStoreService.savePosts(getWallPosts("club22062158"));
        vkDataStoreService.savePosts(getWallPosts("kvarnado"));
        vkDataStoreService.savePosts(getWallPosts("sdalsnyal"));
    }

    public List<ItemDTO> getWallPosts(String groupId) throws EmptyHiddenVkValue, IOException, URISyntaxException {

        return vkPostsService.getWallPostsList(groupId, accessToken).stream().map(ItemJSON::toDto).collect(Collectors.toList());
    }


    private List<WallPostJSON> getPostDiffList() {
        return null;
    }

    public void setVkPostsService(VkPostsService vkPostsService) {
        this.vkPostsService = vkPostsService;
    }

    public void setVkDataStore(VkDataStoreService vkDataStoreService) {
        this.vkDataStoreService = vkDataStoreService;
    }

    public void setVkAuth(VkAuth vkAuth) {
        this.vkAuth = vkAuth;
    }
}
