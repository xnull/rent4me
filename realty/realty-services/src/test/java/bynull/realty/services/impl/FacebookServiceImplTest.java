package bynull.realty.services.impl;

import bynull.realty.ServiceTest;
import bynull.realty.services.api.FacebookService;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

@Ignore
public class FacebookServiceImplTest extends ServiceTest {

    @Resource
    FacebookService service;

    @Test
    public void testSyncElasticSearchWithDB() throws Exception {
        service.syncElasticSearchWithDB();
    }
}