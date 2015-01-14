package bynull.realty.grabber;

import bynull.realty.exeptions.EmptyHiddenVkValue;
import bynull.realty.grabber.services.VkPostsServiceImpl;
import bynull.realty.services.vk.impl.VkDataStoreImpl;
import org.junit.Test;

public class VkLauncherTest {

    @Test
    public void testLaunch() throws Exception, EmptyHiddenVkValue {
        VkLauncher vkLauncher = new VkLauncher();


        VkPostsServiceImpl vkApiGroups = new VkPostsServiceImpl();
        vkApiGroups.init();
        VkDataStoreImpl vkDataStore = new VkDataStoreImpl();
        VkAuth vkAuth = new VkAuth();

        vkLauncher.setVkPostsService(vkApiGroups);
        vkLauncher.setVkDataStore(vkDataStore);
        vkLauncher.setVkAuth(vkAuth);

        vkLauncher.launch();


//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("club22062158"));
//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("kvarnado"));
//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("sdalsnyal"));
    }
}