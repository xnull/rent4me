package bynull.realty.grabber;

import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.services.impl.VkDataStoreImpl;
import bynull.realty.grabber.services.impl.VkGroupPostsServiceImpl;
import org.junit.Test;

public class VkLauncherTest {

    @Test
    public void testLaunch() throws Exception, EmptyHiddenVkValue {
        VkLauncher vkLauncher = new VkLauncher();


        VkGroupPostsServiceImpl vkApiGroups = new VkGroupPostsServiceImpl();
        vkApiGroups.init();
        VkDataStoreImpl vkDataStore = new VkDataStoreImpl();
        VkAuth vkAuth = new VkAuth();

        vkLauncher.setVkGroupPostsService(vkApiGroups);
        vkLauncher.setVkDataStore(vkDataStore);
        vkLauncher.setVkAuth(vkAuth);


        vkLauncher.saveWallPost(vkLauncher.getWallPosts("club22062158"));
        vkLauncher.saveWallPost(vkLauncher.getWallPosts("kvarnado"));
        vkLauncher.saveWallPost(vkLauncher.getWallPosts("sdalsnyal"));
    }
}