package bynull.realty.services.impl.socialnet.vk;

import bynull.realty.ServiceTest;
import bynull.realty.services.api.FacebookService;
import bynull.realty.services.api.VkontakteService;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by null on 10/17/15.
 */
public class VkontakteServiceImplTest extends ServiceTest {

    @Resource
    VkontakteService vkService;

    @Test
    @Ignore
    public void testSvaing(){
        vkService.syncWithVK();
    }
}