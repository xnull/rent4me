package bynull.realty.components;

import bynull.realty.config.Config;
import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.data.business.external.vkontakte.VkontaktePost;
import bynull.realty.data.business.external.vkontakte.VkontaktePostType;
import bynull.realty.utils.JsonUtils;
import bynull.realty.utils.RetryRunner;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dionis on 18/07/14.
 */
@Slf4j
@Component
public class VKHelperComponent {
    private static final String VK_SECRET = "DkAHHjYk8ZvAvAgBQMJd";
    private static final String VK_APP_ID = "4463597";

    @Resource
    Config config;

    @Resource
    AccessTokenPool accessTokenPool;

    private String getRedirectUri() {
        return config.getVkRedirectURL() + "/vk_auth_return_page";
    }

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

    public List<VkWallPostDTO> loadPostsFromPage(String externalId, Date maxPostsAgeToGrab) {
        return doLoadPostsFromPage(externalId, maxPostsAgeToGrab);
    }

    @VisibleForTesting
    List<VkWallPostDTO> doLoadPostsFromPage(String pageId, Date maxAge) {
        String accessToken = "01ddfd6fad91244e793d3a4631fa97096b0f06ba33d09be49fc641485f42cfd7b69256ca3c6a25f48a43d";//accessTokenPool.getValidVkAccessToken();
        accessToken = null;

//        pageId = "22062158";

        return doLoadPostsFromPage0(maxAge, pageId, new ArrayList<>(), 0, 0);
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkWallPostResponse {
        @JsonProperty("response")
        private VkWallPostResponseContent content = new VkWallPostResponseContent();
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkWallPostResponseContent {
        public static final int LIMIT = 100;
        @JsonProperty("count")
        private int totalCount;
        @JsonProperty("items")
        private List<VkWallPostDTO> posts;

        public int getNextOffset(int currentOffset) {
            return currentOffset + LIMIT;
        }
    }

    /**
     * http://vk.com/dev/post_source
     */
    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkPostSource {
        /**
         * Может содержать запись на внешний источник
         */
        @JsonProperty("url")
        private String sourceUrl;
    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkWallPostDTO {
        @JsonProperty("id")
        private String id;
        @JsonProperty("from_id")
        private String fromId;

        @JsonProperty("owner_id")
        private String ownerId;

        @JsonProperty("date")
        private long unixDateSeconds;

        @JsonProperty("post_type")
        private PostType postType;

        @JsonProperty("text")
        private String text;

        @JsonProperty("post_source")
        private VkPostSource vkPostSource;

        public Date getDate() {
            return new Date(unixDateSeconds * 1000);
        }

        public static enum PostType {
            post {
                @Override
                public VkontaktePostType getPostType() {
                    return VkontaktePostType.POST;
                }
            }, copy {
                @Override
                public VkontaktePostType getPostType() {
                    return VkontaktePostType.COPY;
                }
            }, reply {
                @Override
                public VkontaktePostType getPostType() {
                    return VkontaktePostType.REPLY;
                }
            }, postpone {
                @Override
                public VkontaktePostType getPostType() {
                    return VkontaktePostType.POSTPONE;
                }
            }, suggest {
                @Override
                public VkontaktePostType getPostType() {
                    return VkontaktePostType.SUGGEST;
                }
            };

            public abstract VkontaktePostType getPostType();
        }

        public VkontakteApartment toInternal() {
            VkontakteApartment post = new VkontakteApartment();
            post.setExternalId(this.getId());
            post.setDescription(this.getText());
            post.setLogicalCreated(this.getDate());
            return post;
        }
    }

    private List<VkWallPostDTO> doLoadPostsFromPage0(Date maxAge, String pageId, List<VkWallPostDTO> accu, int retryNumber, int offset) {
//        String url = !_url.contains("access_token")
//                ? _url + (_url.contains("?") ? "&" : "?") + "access_token=" + accessToken
//                : _url;
        String url = "http://api.vk.com/method/wall.get?owner_id=-" + pageId + "&v=5.27&count=100&filter=all&extended=1&offset=" + offset;

        log.info("Attempt #[{}] for getting url [{}]", retryNumber, url);

        long requestStartTime = System.currentTimeMillis();

        try {
            GetMethod httpGet = new GetMethod(url);
            String body;
            try {
                httpGet.setFollowRedirects(false);
                int responseCode = httpManager.executeMethod(httpGet);
                if (responseCode != HttpStatus.SC_OK) {
                    throw new BadRequestException("VK request failed. Invalid response code: " + responseCode);
                }
                body = httpGet.getResponseBodyAsString();
            } finally {
                httpGet.releaseConnection();
            }
            log.info("Execution time: {} ms", System.currentTimeMillis() - requestStartTime);
            log.info("Status line: {}", httpGet.getStatusLine());
//            log.info("Body: {}", body);

            log.info("Converting response");
            VkWallPostResponseContent response = JsonUtils.fromJson(body, VkWallPostResponse.class).getContent();
            if (response.getPosts().isEmpty()) {
                return accu;
            }
            log.info("Converted response");

            List<VkWallPostDTO> posts = response.getPosts()
                    .stream()
                    .filter(post -> post.getDate().after(maxAge))
                    .collect(Collectors.toList());
            accu.addAll(posts);

            int size = posts.size();
            boolean hasNextPage = size == VkWallPostResponseContent.LIMIT || size == response.getTotalCount();

            if (!hasNextPage) {
                log.info("Has no next page");
                return accu;
            }

            VkWallPostDTO last = Iterables.getLast(response.getPosts(), null);
            if (last != null) {
                log.info("Last date: [{}]", last.getDate());
            }
            if (last == null || !last.getDate().after(maxAge)) {
                log.info("No sense to goo deeper");
                return accu;
            }
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            return doLoadPostsFromPage0(maxAge, pageId, accu, 0, offset+VkWallPostResponseContent.LIMIT);

        } catch (Exception e) {
            log.warn("Exception occurred while trying to load posts from page", e);
            if (retryNumber < 3) {
                Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
                return doLoadPostsFromPage0(maxAge, pageId, accu, retryNumber + 1, offset);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

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
            redirectUri = URLEncoder.encode(getRedirectUri(), "UTF-8");
            redirectUri = getRedirectUri();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String uri =  "https://oauth.vk.com/access_token?client_id="+VK_APP_ID+"&client_secret="+VK_SECRET+"&code="+person.exchangeCode+"&redirect_uri="+ redirectUri;
        GetMethod httpGet = new GetMethod(uri);
        httpGet.setFollowRedirects(false);
        try {
            int responseCode = httpManager.executeMethod(httpGet);
            long time = System.currentTimeMillis() - requestStartTime;
            LOGGER.info("Execution time: {} ms", time);
            String body = httpGet.getResponseBodyAsString();
            LOGGER.info("Response received for VK auth request: [{}]", body);
            if (responseCode != HttpStatus.SC_OK) {
                throw new BadRequestException("Facebook authentication failed. Invalid response code: " + responseCode);
            }


            VKVerificationInfoDTO response = jacksonObjectMapper.readValue(body, VKVerificationInfoDTO.class);
            Assert.notNull(response);
            Assert.notNull(response.expiresIn, "Expires in should not be empty");
            Assert.notNull(response.vkUserId, "User not empty");
            Assert.notNull(response.accessToken, "Access token should not be empty");
            Assert.notNull(response.email, "Email should not be empty");

            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkUserInfo {
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public VkUserInfo retrieveMoreInfo(String vkId, String accessToken) throws VKAuthorizationException {
        Assert.notNull(vkId, "vk user id required");
        Assert.notNull(accessToken, "vk access token required");

        // Facebook URL example

        VkUserInfo result;
        try {
            result = new RetryRunner<VkUserInfo>(2).doWithRetry(new Callable<VkUserInfo>() {
                @Override
                public VkUserInfo call() throws Exception {
                    return doRetrieveMoreInfo(vkId, accessToken);
                }
            });
        } catch (Exception e) {
            throw new VKAuthorizationException(e);
        }

        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VkUserInfoResponse {
        @JsonProperty("response")
        List<VkUserInfo> response = Collections.emptyList();

        public List<VkUserInfo> getResponse() {
            return response;
        }

        public void setResponse(List<VkUserInfo> response) {
            this.response = response;
        }
    }

    private VkUserInfo doRetrieveMoreInfo(String vkId, String accessToken) {
        long requestStartTime = System.currentTimeMillis();
        //https://api.vk.com/method/'''METHOD_NAME'''?'''PARAMETERS'''&access_token='''ACCESS_TOKEN'''
        String uri = "https://api.vk.com/method/users.get?user_ids="+vkId+"&access_token="+accessToken;


        //https://vk.com/dev/users.get
        //user_ids, fields,
        //name_case: падеж для склонения имени и фамилии пользователя. Возможные значения: именительный – nom, родительный – gen, дательный – dat, винительный – acc, творительный – ins, предложный – abl. По умолчанию nom.

        /**
         * Response fields:
         * https://vk.com/dev/fields :
         id	идентификатор пользователя.
         положительное число
         first_name	имя пользователя.
         строка
         last_name	фамилия пользователя.
         строка
         deactivated	возвращается, если страница пользователя удалена или заблокирована, содержит значение deleted или banned. Обратите внимание, в этом случае дополнительные поля fields не возвращаются.
         hidden: 1	возвращается при вызове без access_token, если пользователь установил настройку «Кому в интернете видна моя страница» — «Только пользователям ВКонтакте». Обратите внимание, в этом случае дополнительные поля fields не возвращаются.

         */

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

            VkUserInfoResponse response = jacksonObjectMapper.readValue(body, VkUserInfoResponse.class);
            Assert.notNull(response);
            VkUserInfo userInfo = Iterables.getFirst(response.getResponse(), null);
            Assert.notNull(userInfo);
//            Assert.notNull(userInfo.firstName, "First name required");
//            Assert.notNull(userInfo.lastName, "Last name required");

            LOGGER.info("Status line: {}", httpGet.getStatusLine());

            return userInfo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpGet.releaseConnection();
        }
    }
}
