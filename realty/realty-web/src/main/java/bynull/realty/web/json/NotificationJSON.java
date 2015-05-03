package bynull.realty.web.json;

import bynull.realty.data.business.notifications.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by dionis on 5/2/15.
 */
@Getter
@Setter
public class NotificationJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("created_dt")
    private Date created;
    @JsonProperty("resolved")
    private boolean resolved;
    @JsonProperty("type")
    private int type;
    @JsonProperty("value")
    private Object value;
}
