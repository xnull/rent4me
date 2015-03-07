package bynull.realty.dto;

import bynull.realty.data.business.external.SocialNetwork;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 04/02/15.
 */
@Getter
@Setter
public class SocialNetPostDTO {
    private String id;
    private SocialNetwork socialNetwork;
    private String externalId;
    private String message;
    private List<String> imageUrls;
    private String link;
    private BigDecimal rentalFee;
    private Integer roomCount;
    private Date created;
    private Date updated;
    private PhoneNumberDTO phoneNumberDTO;
    private Set<? extends MetroDTO> metros;

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
