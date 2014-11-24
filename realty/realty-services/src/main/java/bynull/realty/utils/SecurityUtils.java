package bynull.realty.utils;

import bynull.realty.data.business.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.NotAuthorizedException;

/**
 * @author dionis on 23/11/14.
 */
public class SecurityUtils {
    private SecurityUtils() {
    }

    /**
     * Get authorized user or fail. User object might be detached from hibernate session. So be aware of that.
     * @return
     */
    public static UserIDHolder getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authorizedUser = authentication != null ? (User) authentication.getPrincipal() :  null;
        if(authorizedUser == null) {
            throw new NotAuthorizedException("User not authorized");
        }
        return new UserIDHolder(authorizedUser.getId());
    }

    public static class UserIDHolder {
        private final long id;

        public UserIDHolder(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserIDHolder)) return false;

            UserIDHolder that = (UserIDHolder) o;

            if (id != that.id) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }

        @Override
        public String toString() {
            return "UserIDHolder{" +
                    "id=" + id +
                    '}';
        }
    }
}
