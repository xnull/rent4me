package name.dargiri.web.form;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 19/01/15.
 */
@Getter
@Setter
public class MetroForm {
    private Long id;
    private String stationName;
    private GeoPointForm location;
}
