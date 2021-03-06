package bynull.realty.services.impl.socialnet.fb;

import bynull.realty.common.JsonUtils;
import bynull.realty.components.AccessTokenPool;
import bynull.realty.config.Config;
import bynull.realty.utils.RetryRunner;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 09/07/14.
 */
@Component
@Slf4j
public class FacebookHelperComponent {

    public static final String MY_PROFILE_FACEBOOK_URL = "https://graph.facebook.com/v2.2/me?fields=id,email,name,first_name,last_name,birthday,is_verified,verified&access_token=";
    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};
    @Resource
    Config config;

    @Resource
    AccessTokenPool accessTokenPool;

    public FacebookVerificationInfoDTO verify(final ClientShortInfo person) throws FacebookAuthorizationException {
        Assert.notNull(person.facebookId, "facebook id should not be empty");
        Assert.notNull(person.facebookOAuthToken, "facebook oauth token should not be empty");

        // Facebook URL example
        //  https://graph.facebook.com/me?fields=id&access_token=AAACEdEose0cBALgAbRhQ6csZAg3Uyj100d0LMSSPdp2vZCw5093GEK3h2Wm9dKebfpRZCCn4v1hjaLrynHYSHZB5WWpAl7rf51lXQfY1ke5n39Orb4AZB

        FacebookVerificationInfoDTO result;
        try {
            result = new RetryRunner<FacebookVerificationInfoDTO>(2).doWithRetry(() -> performVerification(person));
        } catch (Exception e) {
            throw new FacebookAuthorizationException(e);
        }

        return result;
    }

    public ExchangedToken exchangeToken(final ClientShortInfo fbInfo) {
        try {
            return new RetryRunner<ExchangedToken>(2).doWithRetry(new Callable<ExchangedToken>() {
                @Override
                public ExchangedToken call() {
                    try {
                        String appId = config.getFbAppId();
                        String appSecret = config.getFbSecret();
                        String exchangeURL = "https://graph.facebook.com/v2.2/oauth/access_token?grant_type=fb_exchange_token&client_id=" + appId + "&client_secret=" + appSecret + "&fb_exchange_token=" + fbInfo.facebookOAuthToken;
                        GetMethod exchangeMethodGet = new GetMethod(exchangeURL);
                        try {
                            int responseCode = httpManager.executeMethod(exchangeMethodGet);
                            if (200 != responseCode) {
                                throw new IllegalStateException("Received non-200 response code: " + responseCode);
                            }
                            String responseBodyAsString = exchangeMethodGet.getResponseBodyAsString();
//                            log.info("Response of FB: [{}]", responseBodyAsString);
                            String[] splittedResult = StringUtils.split(responseBodyAsString, '&');
                            String accessToken = null;
                            String expires = null;
                            for (String s : splittedResult) {
                                String[] keyValuePair = StringUtils.split(s, '=');
                                String key = keyValuePair[0];
                                String value = keyValuePair[1];
                                if ("access_token".equals(key)) {
                                    accessToken = value;
                                } else if ("expires".equals(key)) {
                                    expires = value;
                                }
                            }
                            return new ExchangedToken(accessToken, Integer.valueOf(expires));
                        } finally {
                            exchangeMethodGet.releaseConnection();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (RetryRunner.RetryFailedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            log.info("Execution time: {} ms", System.currentTimeMillis() - requestStartTime);

            FacebookVerificationInfoDTO response = JsonUtils.fromJson(body, FacebookVerificationInfoDTO.class);
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

            log.info("Status line: {}", httpGet.getStatusLine());

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }

    public List<FacebookPostItemDTO> loadPostsFromPage(String pageId, Date maxAge) {
        return doLoadPostsFromPage(pageId, maxAge);
    }

    @VisibleForTesting
    List<FacebookPostItemDTO> doLoadPostsFromPage(String pageId, Date maxAge) {
        String accessToken = accessTokenPool.getValidFbAccessToken();
        String uri = "https://graph.facebook.com/v2.2/" + pageId + "/feed";

        return doLoadPostsFromPage0(maxAge, uri, accessToken, new ArrayList<>(), 0);
    }

    private List<FacebookPostItemDTO> doLoadPostsFromPage0(Date maxAge, String _url, String accessToken, List<FacebookPostItemDTO> accu, int retryNumber) {
        String url = !_url.contains("access_token")
                ? _url + (_url.contains("?") ? "&" : "?") + "access_token=" + accessToken
                : _url;

        log.info("Attempt #[{}] for getting url [{}]", retryNumber, url);

        long requestStartTime = System.currentTimeMillis();

        try {
            String body;
            GetMethod httpGet = new GetMethod(url);
            try {
                httpGet.setFollowRedirects(false);
                int responseCode = httpManager.executeMethod(httpGet);
                if (responseCode != HttpStatus.SC_OK) {
                    throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
                }
                body = httpGet.getResponseBodyAsString();
                log.info("Execution time: {} ms", System.currentTimeMillis() - requestStartTime);
                log.info("Status line: {}", httpGet.getStatusLine());
//                log.info("Body: {}", body);
            } finally {
                httpGet.releaseConnection();
            }

            FacebookFeedItems response = JsonUtils.fromJson(body, FacebookFeedItems.class);
            if (response.getItems().isEmpty()) {
                return accu;
            }

            List<FacebookPostItemDTO> items = response.getItems()
                    .stream()
                    .filter(post -> post.getCreatedDtime().after(maxAge))
                    .collect(Collectors.toList());
            accu.addAll(items);

            if (!response.getPaging().hasNextPage()) {
                return accu;
            }

            FacebookPostItemDTO last = Iterables.getLast(items, null);
            if (last == null || !last.getCreatedDtime().after(maxAge)) {
                return accu;
            }

            return doLoadPostsFromPage0(maxAge, response.getPaging().getNext(), accessToken, accu, 0);

        } catch (Exception e) {
            log.warn("Exception occurred while trying to load posts from page", e);
            if (retryNumber < 3) {
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                return doLoadPostsFromPage0(maxAge, _url, accessToken, accu, retryNumber + 1);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public static class ExchangedToken {
        public final String accessToken;
        public final int liveTimeSeconds;

        public ExchangedToken(String accessToken, int liveTimeSeconds) {
            this.accessToken = accessToken;
            this.liveTimeSeconds = liveTimeSeconds;
        }
    }

    public static class ClientShortInfo {
        public final String facebookId;
        public final String facebookOAuthToken;

        public ClientShortInfo(String facebookId, String facebookOAuthToken) {
            this.facebookId = facebookId;
            this.facebookOAuthToken = facebookOAuthToken;
        }
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

    /**
     * https://developers.facebook.com/docs/graph-api/reference/v2.2/post
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FacebookPostItemDTO {
        private final String id;
        private final String message;
        private final String picture;
        private final String link;
        private final String type;
        private final Author author;
        private final Date createdDtime;
        private final Date updatedDtime;
        private final List<Action> actions;

        public FacebookPostItemDTO(
                @JsonProperty("id")
                String id,
                @JsonProperty("message")
                String message,
                //yyyy-MM-dd'T'HH:mm:ssZ
//                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
                @JsonProperty("created_time")
                Date createdDtime,
                //yyyy-MM-dd'T'HH:mm:ssZ
//                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
                @JsonProperty("updated_time")
                Date updatedDtime,
                @JsonProperty("picture")
                String picture,
                @JsonProperty("link")
                String link,
                @JsonProperty("type")
                String type,
                @JsonProperty("from")
                Author author,
                @JsonProperty("actions")
                List<Action> actions
        ) {
            this.id = id;
            this.message = message;
            this.createdDtime = createdDtime;
            this.updatedDtime = updatedDtime;
            this.picture = picture;
            this.link = link;
            this.type = type;
            this.actions = actions;
            this.author = author;
        }

        public List<Action> getActions() {
            return actions == null ? Collections.emptyList() : actions;
        }

        public String getExternalLink() {
            String link = getLink();
            for (Action action : this.getActions()) {
                if ("like".equalsIgnoreCase(action.getName())) {
                    link = action.getLink();
                    break;
                }
            }
            return link;
        }

        public String getExternalAuthorLink() {
            String link = null;
            if (author != null) {
                link = "https://www.facebook.com/" + author.getId();
            }

            return link;
        }

        @Getter
        @Setter
        public static class Action {
            @JsonProperty("name")
            private String name;
            @JsonProperty("link")
            private String link;
        }

        @Getter
        @Setter
        public static class Author {
            @JsonProperty("name")
            private String name;
            @JsonProperty("id")
            private String id;
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public Date getCreatedDtime() {
            return copy(createdDtime);
        }

        public Date getUpdatedDtime() {
            return copy(updatedDtime);
        }

        public String getPicture() {
            return picture;
        }

        public String getLink() {
            return link;
        }

        public String getType() {
            return type;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FacebookFeedItems {
        private final List<FacebookPostItemDTO> items;
        private final FacebookPaging paging;

        public FacebookFeedItems(
                @JsonProperty("data")
                List<FacebookPostItemDTO> items,
                @JsonProperty("paging")
                FacebookPaging paging) {
            this.items = items;
            this.paging = paging;
        }

        public List<FacebookPostItemDTO> getItems() {
            return Collections.unmodifiableList(items);
        }

        public FacebookPaging getPaging() {
            return paging != null ? paging : FacebookPaging.NONE;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FacebookPaging {
        public static final FacebookPaging NONE = new FacebookPaging(null, null);

        private final String prev;
        private final String next;

        public FacebookPaging(
                @JsonProperty("previous")
                String prev,
                @JsonProperty("next")
                String next) {
            this.prev = prev;
            this.next = next;
        }

        public String getNext() {
            return next;
        }

        public boolean hasNextPage() {
            return getNext() != null;
        }
    }
}
