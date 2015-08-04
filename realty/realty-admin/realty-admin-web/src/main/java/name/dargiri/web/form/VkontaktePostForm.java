package name.dargiri.web.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 19/01/15.
 */
@Getter
@Setter
public class VkontaktePostForm {
    private String message;
    private Integer roomCount;
    private BigDecimal rentalFee;
    private List<String> imageUrls;
    private String link;
    private Date created;
    private Date updated;
    private VkontaktePageForm page;
    private Set<? extends MetroForm> metros;
    private PhoneNumberForm phoneNumber;

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

    @JsonIgnore
    public Optional<VkontaktePageForm> getPageOpt(){
        return Optional.ofNullable(page);
    }
}
