package bynull.realty.grabber;

import bynull.realty.exeptions.EmptyHiddenVkValue;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: trierra
 * Date: 11/4/14
 * Browser imitation parser receive the access_token
 */
@Component
public class VkAuth {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkAuth.class);

    /**
     * Vk request example:
     * https://api.vk.com/method/'''METHOD_NAME'''?'''PARAMETERS'''&access_token='''ACCESS_TOKEN'''
     */
    public VkAuth() {

    }

    public String receiveToken() throws IOException, URISyntaxException, EmptyHiddenVkValue {
        CloseableHttpClient httpClient = getCloseableHttpClient();
        HttpPost post = new HttpPost(getAuthorizeURI());
        HttpResponse response = httpClient.execute(post);

        String loginHtmlString = IOUtils.toString(response.getEntity().getContent());
        String ipH = loginHtmlString.substring(loginHtmlString.indexOf("name=\"ip_h\"") + 19, loginHtmlString.indexOf("\" />", loginHtmlString.indexOf("name=\"ip_h\"")));
        String to = loginHtmlString.substring(loginHtmlString.indexOf("name=\"to\"") + 17, loginHtmlString.indexOf("\">", loginHtmlString.indexOf("name=\"to\"")));

        if (ipH.isEmpty()) {
            throw new EmptyHiddenVkValue("'ip_h'");
        } else if (to.isEmpty()) {
            throw new EmptyHiddenVkValue("'to'");
        }

        Header redirectLocation = getHeaderWIthToken(httpClient, ipH, to);
        return redirectLocation.getValue().substring(redirectLocation.getValue().indexOf("_token=") + 7, redirectLocation.getValue().indexOf("&"));

    }

    /**
     * Need to do 3 redirects to get the access_token url like -- Location: https://oauth.vk.com/blank.html#access_token=3bb176655c58379769a7c&expires_in=86400&user_id=7692140
     */
    private Header getHeaderWIthToken(CloseableHttpClient httpClient, String ipH, String to) throws URISyntaxException, IOException {
        HttpPost post;
        post = new HttpPost(getLoginURI(ipH, to));
        Header redirectLocation = getHeader(httpClient, post);
        post = new HttpPost(redirectLocation.getValue());
        redirectLocation = getHeader(httpClient, post);
        post = new HttpPost(redirectLocation.getValue());
        redirectLocation = getHeader(httpClient, post);
        return redirectLocation;
    }

    public URI getAuthorizeURI() throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("oauth.vk.com").setPath("/authorize")
                .setParameter("client_id", "4623432")
                .setParameter("scope", "groups")
                .setParameter("redirect_uri", "http://oauth.vk.com/blank.html")
                .setParameter("display", "popup")
                .setParameter("response_type", "token");
        return builder.build();
    }

    public URI getLoginURI(String ipH, String to) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("login.vk.com")
                .setParameter("act", "login")
                .setParameter("soft", "1")
                .setParameter("ip_h", ipH)
                .setParameter("to", to)
                .setParameter("expire", "0")
                .setParameter("email", "chuiko.oksana@gmail.com")
                .setParameter("pass", "Jfreem13");
        return builder.build();
    }

    public CloseableHttpClient getCloseableHttpClient() {
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .build();
    }

    public Header getHeader(CloseableHttpClient httpClient, HttpPost post) throws IOException {
        HttpResponse response = httpClient.execute(post);
        Header[] locations = response.getHeaders("Location");
        Header location = locations[0];
        post.reset();
        return location;
    }

    public static void main(String[] args) throws EmptyHiddenVkValue, IOException, URISyntaxException {
        VkAuth crowler = new VkAuth();
        crowler.receiveToken();
    }

}
