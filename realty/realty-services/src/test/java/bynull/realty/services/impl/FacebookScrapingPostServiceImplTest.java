package bynull.realty.services.impl;

import bynull.realty.ServiceTest;
import bynull.realty.services.api.FacebookScrapingPostService;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Ignore
public class FacebookScrapingPostServiceImplTest extends ServiceTest {

    @Resource
    FacebookScrapingPostService service;

    @Test
    public void testSyncElasticSearchWithDB() throws Exception {
        service.syncElasticSearchWithDB();
    }
}