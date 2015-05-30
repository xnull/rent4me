package bynull.realty.dto;

import bynull.realty.data.business.PhotoTemp;
import lombok.Getter;

import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 05/12/14.
 */
@Getter
public class PhotoTempDTO {
    public static PhotoTempDTO from(PhotoTemp photoTemp) {
        if (photoTemp == null) return null;
        PhotoTempDTO dto = new PhotoTempDTO(photoTemp.getId(), photoTemp.getUrl(), photoTemp.getGuid(), photoTemp.getCreated());
        return dto;
    }

    private final Long id;
    private final String url;
    private final String guid;
    private final Date created;

    private PhotoTempDTO(Long id, String url, String guid, Date created) {
        this.id = id;
        this.url = url;
        this.guid = guid;
        this.created = copy(created);
    }

    public Date getCreated() {
        return copy(created);
    }
}
