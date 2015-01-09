package bynull.realty.vk;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 20.11.14.
 */
public class VkontakeApiTest {

    @Test
    public void testRequest() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.vk.com/method/newsfeed.search?q=rent&count=10");

        String result = target.request(MediaType.APPLICATION_JSON).get(String.class);

        System.out.println(result);
    }

//    @Test
//    public void testRequestBuilder(){
//        VkCrowler grabber = new VkCrowler();
//        ParametersCollector collector = new ParametersCollector();
//        collector.setUrl(String url);
//
//
//        grabber.buildRequest(collector);
//
//    }

    @Test
    public void testParseField() {

    }

    @Test
    public void testParseToken() {

    }


}
