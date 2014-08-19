package bynull.realty.components;

import bynull.realty.utils.RetryRunner;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Callable;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 18/07/14.
 */
@Component
public class VKHelperComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(VKHelperComponent.class);
    public static final String MY_PROFILE_FACEBOOK_URL = "https://graph.facebook.com/me?fields=id,email,name,first_name,last_name,birthday,is_verified,verified&access_token=";


    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};

    public static class ClientShortInfo {
        public final String vkId;
        public final String vkOAuthToken;

        public ClientShortInfo(String vkId, String vkOAuthToken) {
            this.vkId = vkId;
            this.vkOAuthToken = vkOAuthToken;
        }
    }

    public VKVerificationInfoDTO verify(final ClientShortInfo person) throws VKAuthorizationException {
        Assert.notNull(person.vkId, "facebook id should not be empty");
        Assert.notNull(person.vkOAuthToken, "facebook oauth token should not be empty");

        // Facebook URL example
        //  https://graph.facebook.com/me?fields=id&access_token=AAACEdEose0cBALgAbRhQ6csZAg3Uyj100d0LMSSPdp2vZCw5093GEK3h2Wm9dKebfpRZCCn4v1hjaLrynHYSHZB5WWpAl7rf51lXQfY1ke5n39Orb4AZB

        VKVerificationInfoDTO result;
        try {
            result = new RetryRunner<VKVerificationInfoDTO>(2).doWithRetry(new Callable<VKVerificationInfoDTO>() {
                @Override
                public VKVerificationInfoDTO call() throws Exception {
                    return performVerification(person);
                }
            });
        } catch (Exception e) {
            throw new VKAuthorizationException(e);
        }

        return result;
    }

    public static class VKAuthorizationException extends Exception {
        public VKAuthorizationException() {
        }

        public VKAuthorizationException(String message) {
            super(message);
        }

        public VKAuthorizationException(String message, Throwable cause) {
            super(message, cause);
        }

        public VKAuthorizationException(Throwable cause) {
            super(cause);
        }

        public VKAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public static class VKVerificationInfoDTO {
        public final String vkId;
        public final String email;
        public final String name;
        public final String firstName;
        public final String lastName;
        public final Date birthday;
        public final boolean verified;

        public VKVerificationInfoDTO(@JsonProperty("id") String vkId,
                                     @JsonProperty("email") String email,
                                     @JsonProperty("name") String name,
                                     @JsonProperty("first_name") String firstName,
                                     @JsonProperty("last_name") String lastName,
                                     @JsonProperty("birthday")
                                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy") Date birthday,
                                     @JsonProperty("verified") boolean verified,
                                     @JsonProperty("is_verified") boolean verifiedManuallyByFB
        ) {
            this.vkId = vkId;
            this.email = email;
            this.name = name;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = copy(birthday);
            this.verified = verified || verifiedManuallyByFB;
        }
    }

    private VKVerificationInfoDTO performVerification(ClientShortInfo person) {
        long requestStartTime = System.currentTimeMillis();
        String uri = MY_PROFILE_FACEBOOK_URL + person.vkOAuthToken;
        GetMethod httpGet = new GetMethod(uri);
        httpGet.setFollowRedirects(false);
        try {
            int responseCode = httpManager.executeMethod(httpGet);
            if (responseCode != HttpStatus.SC_OK) {
                throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
            }
            String body = httpGet.getResponseBodyAsString();
            LOGGER.info("Execution time: {} ms", (System.currentTimeMillis() - requestStartTime));

            VKVerificationInfoDTO response = jacksonObjectMapper.readValue(body, VKVerificationInfoDTO.class);
            Assert.notNull(response);
            Assert.notNull(response.vkId, "Facebook id should not be empty");
            Assert.notNull(response.email, "Email should not be empty");
            Assert.notNull(response.name, "Name should not be empty");

            if (!response.vkId.equals(person.vkId)) {
                throw new BadRequestException("Facebook id differs");
            }

            if (!response.verified) {
                throw new WebApplicationException("Account not verified", Response.Status.EXPECTATION_FAILED);
            }

            Assert.isTrue(response.vkId.equals(person.vkId), "Invalid facebook id");

            // returned string example {"id":"100000169700800"}

            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
