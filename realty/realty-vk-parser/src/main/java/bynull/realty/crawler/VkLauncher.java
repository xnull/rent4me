package bynull.realty.crawler;

import bynull.realty.crawler.api.VkApiGroups;
import bynull.realty.crawler.api.VkDataStore;
import bynull.realty.crawler.json.BaseEntity;
import bynull.realty.crawler.json.WallPost;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by trierra on 12/9/14.
 */

@Component
public class VkLauncher {

    @Autowired
    private VkApiGroups vkApiGroups;

    @Autowired
    private VkDataStore vkDataStore;
    private String accessToken;

    @Scheduled
    public void launch() throws EmptyHiddenVkValue, IOException, URISyntaxException {
        VkAuth vkAuth = new VkAuth();
        accessToken = vkAuth.receiveToken();
        List<BaseEntity> club22062158 = vkApiGroups.wallGetPostsList("club22062158", accessToken);
        vkDataStore.savePost(club22062158);
        List<BaseEntity> kvarnado = vkApiGroups.wallGetPostsList("kvarnado", accessToken);
        List<BaseEntity> sdalsnyal = vkApiGroups.wallGetPostsList("sdalsnyal", accessToken);

    }

    private List<WallPost> getPostDiffList() {
        return null;
    }

    public void setVkApiGroups(VkApiGroups vkApiGroups) {
        this.vkApiGroups = vkApiGroups;
    }

    public void setVkDataStore(VkDataStore vkDataStore) {
        this.vkDataStore = vkDataStore;
    }
}
