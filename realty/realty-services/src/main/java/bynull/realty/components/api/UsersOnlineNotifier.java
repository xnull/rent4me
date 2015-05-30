package bynull.realty.components.api;

import java.util.Collection;

/**
 * Created by dionis on 4/30/15.
 */
public interface UsersOnlineNotifier<T> {
    Collection<Long> getUserIdsOnline();

    boolean isUserOnline(long userId);

    boolean sendToUserIfOnline(long userId, T content);

    boolean broadcastToChannel(String channel, T content);

    boolean sendToUserOnChannel(String channel, long userId, T content);
}
