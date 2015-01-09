package bynull.realty.web.rest;

import bynull.realty.data.business.User;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author dionis on 09/07/14.
 */
@Component
@Path("auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

    @POST
    @Path("/login")
    public Response login(RequestLoginJSON requestLoginJSON) {
        String token = userTokenService.getTokenIfValidCredentials(requestLoginJSON.getUsername(), requestLoginJSON.getPassword());
        return Response.ok(new TokenJSON(token)).build();
    }

    @POST
    @Path("/facebook")
    public Response authenticateWithFacebook(RequestFacebookLoginJSON facebookLoginJSON) {
        UserService.UsernameTokenPair usernameTokenPair = userService.authenticateFacebookUser(facebookLoginJSON.getFacebookId(), facebookLoginJSON.getAccessToken());
        return Response.ok(UsernameTokenPairJSON.from(usernameTokenPair)).build();
    }

    @POST
    @Path("/vk")
    public Response authenticateWithVK(RequestVKLoginJSON vkLoginJSON) {
        UserService.UsernameTokenPair usernameTokenPair = userService.authenticateVkUser(vkLoginJSON.getCode());
        return Response.ok(UsernameTokenPairJSON.from(usernameTokenPair)).build();
    }

    @DELETE
    public Response logout(TokenJSON tokenJSON, @Context HttpServletRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = authentication != null ? (User) authentication.getPrincipal() : null;
        userTokenService.deleteToken(user, tokenJSON.getToken());

        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContextHolder.clearContext();

        return Response.ok().build();
    }

    public static class UsernameTokenPairJSON {
        @JsonProperty("username")
        private String username;
        @JsonProperty("token")
        private String token;

        public static UsernameTokenPairJSON from(UserService.UsernameTokenPair pair) {
            if (pair == null) {
                return null;
            }
            return new UsernameTokenPairJSON(pair.username, pair.token);
        }

        public UsernameTokenPairJSON(String username, String token) {
            this.username = username;
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class TokenJSON {
        @JsonProperty("token")
        private String token;

        public TokenJSON() {
        }

        public TokenJSON(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class RequestVKLoginJSON {
        @JsonProperty("code")
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class RequestFacebookLoginJSON {
        @JsonProperty("facebook_id")
        private String facebookId;
        @JsonProperty("access_token")
        private String accessToken;

        public String getFacebookId() {
            return facebookId;
        }

        public void setFacebookId(String facebookId) {
            this.facebookId = facebookId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
    }

    public static class RequestLoginJSON {
        @JsonProperty("username")
        private String username;
        @JsonProperty("password")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
