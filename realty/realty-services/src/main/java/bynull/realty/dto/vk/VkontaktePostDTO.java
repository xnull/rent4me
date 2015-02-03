package bynull.realty.dto.vk;

import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.fb.FacebookPageDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 04/01/15.
 */
@Getter
@Setter
public class VkontaktePostDTO {
    private Long id;
    private String message;
    private List<String> imageUrls;
    private String link;
    private BigDecimal rentalFee;
    private Integer roomCount;
    private Date created;
    private Date updated;
    private VkontaktePageDTO page;
    private Set<MetroDTO> metros;

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
