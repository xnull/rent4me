package bynull.realty.dto.fb;

import bynull.realty.dto.MetroDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 04/01/15.
 */
@Getter
@Setter
public class FacebookPostDTO {
    private Long id;
    private String message;
    private List<String> imageUrls;
    private String link;
    private Date created;
    private Date updated;
    private FacebookPageDTO page;
    private Set<MetroDTO> metro;

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