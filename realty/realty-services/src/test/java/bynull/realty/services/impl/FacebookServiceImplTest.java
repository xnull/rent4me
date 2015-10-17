package bynull.realty.services.impl;

import bynull.realty.ServiceTest;
import bynull.realty.services.api.FacebookService;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

public class FacebookServiceImplTest extends ServiceTest {

    @Resource
    FacebookService fbService;

    @Test
    public void testSvaing(){
        fbService.syncWithFB();
    }
}