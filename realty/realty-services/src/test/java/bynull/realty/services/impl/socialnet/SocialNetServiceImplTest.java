package bynull.realty.services.impl.socialnet;

import bynull.realty.ServiceTest;
import bynull.realty.services.api.SocialNetService;
import org.junit.Test;

import javax.annotation.Resource;

public class SocialNetServiceImplTest extends ServiceTest {
    @Resource
    SocialNetService service;

    @Test
    public void checkSQLValidity() {
        //service.refreshMaterializedView();
    }
}