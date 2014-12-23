package bynull.realty.crawler;

import bynull.realty.crawler.services.VkApiGroupsImpl;
import bynull.realty.crawler.services.VkDataStoreImpl;
import bynull.realty.exeptions.EmptyHiddenVkValue;
import org.junit.Test;

public class VkLauncherTest {

    @Test
    public void testLaunch() throws Exception, EmptyHiddenVkValue {
        VkLauncher vkLauncher = new VkLauncher();
        VkApiGroupsImpl vkApiGroups = new VkApiGroupsImpl();
        vkApiGroups.init();
        VkDataStoreImpl vkDataStore = new VkDataStoreImpl();

        vkLauncher.setVkApiGroups(vkApiGroups);
        vkLauncher.launch();
    }
}