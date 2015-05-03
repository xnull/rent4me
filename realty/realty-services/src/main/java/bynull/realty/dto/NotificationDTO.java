package bynull.realty.dto;

import bynull.realty.data.business.notifications.Notification;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dionis on 5/2/15.
 */
@Getter
@Setter
public class NotificationDTO<T> {
    private Long id;
    private Date created;
    private boolean resolved;
    private UserDTO receiver;
    private Notification.Type type;
    private T value;
}
