package bynull.realty.grabber;

import bynull.realty.VkTest;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class VkLauncherTest extends VkTest {

    @Autowired
    VkLauncher vkLauncher;

    @Test
    public void vkAutowireTest() {
        assertThat(vkLauncher, is(notNullValue()));
    }


    @Test
    public void testLaunch() throws Exception, EmptyHiddenVkValue {
//        VkLauncher vkLauncher = new VkLauncher();
//
//
//        VkPostsServiceImpl vkApiGroups = new VkPostsServiceImpl();
//        vkApiGroups.init();
//        VkDataStoreServiceImpl vkDataStore = new VkDataStoreServiceImpl();
//        VkAuth vkAuth = new VkAuth();
//
//        vkLauncher.setVkPostsService(vkApiGroups);
//        vkLauncher.setVkDataStore(vkDataStore);
//        vkLauncher.setVkAuth(vkAuth);

        vkLauncher.launch();


//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("club22062158"));
//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("kvarnado"));
//        vkLauncher.saveWallPost(vkLauncher.getWallPosts("sdalsnyal"));
    }
}