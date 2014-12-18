package bynull.realty.vk;

import bynull.realty.crawler.VkAuth;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class VkAuthTest {

    VkAuth crawler;

    @Before
    public void before() {
        crawler = new VkAuth();
    }

    @Test
    public void testGetAuthorizeURI() throws URISyntaxException {
        String uri = "http://oauth.vk.com/authorize?client_id=4623432&scope=groups&redirect_uri=http%3A%2F%2Foauth.vk.com%2Fblank.html&display=popup&response_type=token";
        assertEquals(crawler.getAuthorizeURI().toString(), uri);
        assertFalse(crawler.getAuthorizeURI().toString().equals(uri + "aaa"));
    }

    @Test
    public void testGetLoginURI() throws Exception {
        String loginUri = "https://login.vk.com?act=login&soft=1&ip_h=ip_h&to=to&expire=0&email=chuiko.oksana%40gmail.com&pass=Jfreem13";
        assertEquals(crawler.getLoginURI("ip_h", "to").toString(), loginUri);
        assertFalse(crawler.getLoginURI("iph", "to").toString().equals(loginUri));
    }
}