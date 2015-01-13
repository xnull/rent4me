package bynull.realty.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Elastic search config
 * Created by dionis on 03/01/15.
 */
@Getter
@Setter
public class ESConfig {
    private String dbJdbcUrl;
    private String dbUsername;
    private String dbPassword;
    private String index;
    private String type;
    private String river;
    private String esConnectionUrl;

}
