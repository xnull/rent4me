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
 * @author dionis on 09/07/14.
 */
@Component
public class FacebookHelperComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookHelperComponent.class);
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
        public final String facebookId;
        public final String facebookOAuthToken;

        public ClientShortInfo(String facebookId, String facebookOAuthToken) {
            this.facebookId = facebookId;
            this.facebookOAuthToken = facebookOAuthToken;
        }
    }

    public FacebookVerificationInfoDTO verify(final ClientShortInfo person) throws FacebookAuthorizationException {
        Assert.notNull(person.facebookId, "facebook id should not be empty");
        Assert.notNull(person.facebookOAuthToken, "facebook oauth token should not be empty");

        // Facebook URL example
        //  https://graph.facebook.com/me?fields=id&access_token=AAACEdEose0cBALgAbRhQ6csZAg3Uyj100d0LMSSPdp2vZCw5093GEK3h2Wm9dKebfpRZCCn4v1hjaLrynHYSHZB5WWpAl7rf51lXQfY1ke5n39Orb4AZB

        FacebookVerificationInfoDTO result;
        try {
            result = new RetryRunner<FacebookVerificationInfoDTO>(2).doWithRetry(new Callable<FacebookVerificationInfoDTO>() {
                @Override
                public FacebookVerificationInfoDTO call() throws Exception {
                    return performVerification(person);
                }
            });
        } catch (Exception e) {
            throw new FacebookAuthorizationException(e);
        }

        return result;
    }

    public static class FacebookAuthorizationException extends Exception {
        public FacebookAuthorizationException() {
        }

        public FacebookAuthorizationException(String message) {
            super(message);
        }

        public FacebookAuthorizationException(String message, Throwable cause) {
            super(message, cause);
        }

        public FacebookAuthorizationException(Throwable cause) {
            super(cause);
        }

        public FacebookAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public static class FacebookVerificationInfoDTO {
        public final String facebookId;
        public final String email;
        public final String name;
        public final String firstName;
        public final String lastName;
        public final Date birthday;
        public final boolean verified;

        public FacebookVerificationInfoDTO(@JsonProperty("id") String facebookId,
                                           @JsonProperty("email") String email,
                                           @JsonProperty("name") String name,
                                           @JsonProperty("first_name") String firstName,
                                           @JsonProperty("last_name") String lastName,
                                           @JsonProperty("birthday")
                                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy") Date birthday,
                                           @JsonProperty("verified") boolean verified,
                                           @JsonProperty("is_verified") boolean verifiedManuallyByFB
        ) {
            this.facebookId = facebookId;
            this.email = email;
            this.name = name;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthday = copy(birthday);
            this.verified = verified || verifiedManuallyByFB;
        }
    }

    private FacebookVerificationInfoDTO performVerification(ClientShortInfo person) {
        long requestStartTime = System.currentTimeMillis();
        String uri = MY_PROFILE_FACEBOOK_URL + person.facebookOAuthToken;
        GetMethod httpGet = new GetMethod(uri);
        httpGet.setFollowRedirects(false);
        try {
            int responseCode = httpManager.executeMethod(httpGet);
            if (responseCode != HttpStatus.SC_OK) {
                throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
            }
            String body = httpGet.getResponseBodyAsString();
            LOGGER.info("Execution time: {} ms", (System.currentTimeMillis() - requestStartTime));

            FacebookVerificationInfoDTO response = jacksonObjectMapper.readValue(body, FacebookVerificationInfoDTO.class);
            Assert.notNull(response);
            Assert.notNull(response.facebookId, "Facebook id should not be empty");
            Assert.notNull(response.email, "Email should not be empty");
            Assert.notNull(response.name, "Name should not be empty");

            if (!response.facebookId.equals(person.facebookId)) {
                throw new BadRequestException("Facebook id differs");
            }

            if (!response.verified) {
                throw new WebApplicationException("Account not verified", Response.Status.EXPECTATION_FAILED);
            }

            Assert.isTrue(response.facebookId.equals(person.facebookId), "Invalid facebook id");

            // returned string example {"id":"100000169700800"}

            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }

    /*public List<PersonInformationDTO> findFacebookFriends(final Person person){
        Assert.notNull(person);
        RetryRunner<List<PersonInformationDTO>> retryRunner = new RetryRunner<>(3);
        try {
            List<PersonInformationDTO> personInformationDTOs = retryRunner.doWithRetry(new Callable<List<PersonInformationDTO>>() {
                @Override
                public List<PersonInformationDTO> call() throws Exception {
                    return doFindFacebookFriends(person);
                }
            });
            return personInformationDTOs;
        } catch (RetryRunner.RetryFailedException e) {
            LOGGER.error("Exception occurred while running in parallel", e);
            return Collections.emptyList();
        }
    }

    List<PersonInformationDTO> doFindFacebookFriends(Person _person) {
        Assert.notNull(_person);
        Assert.notNull(_person.getFbAccessToken());
        String uri = "https://graph.facebook.com/me/friends?fields=username,name,id&access_token=" + _person.getFbAccessToken();
        return doFindFacebookFriends0(new ArrayList<PersonInformationDTO>(), uri);
    }

    private List<PersonInformationDTO> doFindFacebookFriends0(List<PersonInformationDTO> accu, String url) {

        long requestStartTime = System.currentTimeMillis();

        GetMethod httpGet = new GetMethod(url);
        httpGet.setFollowRedirects(false);
        try {
            externalApiLogger.log(DataSource.FACEBOOK, url, ExternalApiUsageLog.ExternalApiType.FACEBOOK);
            int responseCode = httpManager.getNormalHttpClient().executeMethod(httpGet);
            if (responseCode != HttpStatus.SC_OK) {
                throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
            }
            String body = httpGet.getResponseBodyAsString();
            LOGGER.info("Execution time: {} ms", (System.currentTimeMillis() - requestStartTime));
            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            FacebookFriendsResponse response = jacksonObjectMapper.readValue(body, FacebookFriendsResponse.class);
            accu.addAll(response.toPersonInformationDTOs());


            return response.isNotEmpty() && response.hasNextPage() ? doFindFacebookFriends0(accu, response.getPaging().getNextPage()) : accu;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }*/
}
