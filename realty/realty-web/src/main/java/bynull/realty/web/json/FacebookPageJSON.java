package bynull.realty.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 20/01/15.
 */
@Getter
@Setter
public class FacebookPageJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("link")
    private String link;
}
