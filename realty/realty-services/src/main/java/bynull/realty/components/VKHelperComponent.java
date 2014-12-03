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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.Callable;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 18/07/14.
 */
@Component
public class VKHelperComponent {
    private static final String VK_SECRET = "DkAHHjYk8ZvAvAgBQMJd";
    private static final String VK_APP_ID = "4463597";
    //TODO: change url
    private static final String REDIRECT_URI = "http://localhost:8888/dev" + "/vk_auth_return_page";

    private static final Logger LOGGER = LoggerFactory.getLogger(VKHelperComponent.class);


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
        public final String exchangeCode;

        public ClientShortInfo(String exchangeCode) {
            this.exchangeCode = exchangeCode;
        }
    }

    public VKVerificationInfoDTO verify(final ClientShortInfo person) throws VKAuthorizationException {
        Assert.notNull(person.exchangeCode, "vk exchange code should not be empty");

        // Facebook URL example

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
        public final String vkUserId;
        public final String accessToken;
        public final String email;
        public final long expiresIn;

        public VKVerificationInfoDTO(@JsonProperty("user_id") String vkUserId,
                                     @JsonProperty("access_token") String accessToken,
                                     @JsonProperty("email") String email,
                                     @JsonProperty("expires_in") int expiresIn
        ) {
            this.vkUserId = vkUserId;
            this.accessToken = accessToken;
            this.email = email;
            this.expiresIn = expiresIn;
        }
    }

    private VKVerificationInfoDTO performVerification(ClientShortInfo person) {
        long requestStartTime = System.currentTimeMillis();
        String redirectUri = null;
        try {
            redirectUri = URLEncoder.encode(REDIRECT_URI, "UTF-8");
            redirectUri = REDIRECT_URI;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String uri =  "https://oauth.vk.com/access_token?client_id="+VK_APP_ID+"&client_secret="+VK_SECRET+"&code="+person.exchangeCode+"&redirect_uri="+ redirectUri;
        GetMethod httpGet = new GetMethod(uri);
        httpGet.setFollowRedirects(false);
        try {
            int responseCode = httpManager.executeMethod(httpGet);
            if (responseCode != HttpStatus.SC_OK) {
                throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
            }
            String body = httpGet.getResponseBodyAsString();
            long time = System.currentTimeMillis() - requestStartTime;
            LOGGER.info("Execution time: {} ms", time);

            LOGGER.info("Response received for VK auth request: [{}]", body);

            VKVerificationInfoDTO response = jacksonObjectMapper.readValue(body, VKVerificationInfoDTO.class);
            Assert.notNull(response);
            Assert.notNull(response.expiresIn, "Expires in should not be empty");
            Assert.notNull(response.vkUserId, "User not empty");
            Assert.notNull(response.accessToken, "Access token should not be empty");
            Assert.notNull(response.email, "Email should not be empty");

//            if (!response.vkId.equals(person.vkId)) {
//                throw new BadRequestException("Facebook id differs");
//            }
//
//            if (!response.verified) {
//                throw new WebApplicationException("Account not verified", Response.Status.EXPECTATION_FAILED);
//            }
//
//            Assert.isTrue(response.vkId.equals(person.vkId), "Invalid facebook id");
//
//            // returned string example {"id":"100000169700800"}

            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
