package bynull.realty.dto;

import bynull.realty.data.business.PhotoTemp;

import java.util.Date;

/**
 * @author dionis on 05/12/14.
 */
public class PhotoTempDTO {
    public static PhotoTempDTO from(PhotoTemp photoTemp) {
        if(photoTemp == null) return null;
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
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getGuid() {
        return guid;
    }

    public Date getCreated() {
        return created;
    }
}
