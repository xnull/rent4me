package bynull.realty.dao;

import bynull.realty.data.business.User;
import bynull.realty.data.business.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dionis on 5/2/15.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select n from Notification n where n.receiver=:user and n.resolved=false order by n.created desc")
    List<Notification> listMyUnreadNotifications(@Param("user")User user);
}
