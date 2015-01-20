package bynull.realty.web.json;

import bynull.realty.dao.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 20/01/15.
 */
@Getter
@Setter
public class FacebookPostJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("img_urls")
    private List<String> imageUrls;
    @JsonProperty("link")
    private String link;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("created")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("updated")
    private Date updated;
    @JsonProperty("page")
    private FacebookPageJSON page;
    @JsonProperty("metros")
    private Set<MetroJSON> metros;

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    public void setUpdated(Date updated) {
        this.updated = copy(updated);
    }
}
