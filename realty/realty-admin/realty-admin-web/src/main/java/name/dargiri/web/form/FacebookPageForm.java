package name.dargiri.web.form;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 18/01/15.
 */
@Getter
@Setter
public class FacebookPageForm {
    private Long id;
    private String externalId;
    private String link;
    private boolean enabled;
    private CityForm city;
}
