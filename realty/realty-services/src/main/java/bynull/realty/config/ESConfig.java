package bynull.realty.config;

/**
 * Elastic search config
 * Created by dionis on 03/01/15.
 */
public class ESConfig {
    private String dbJdbcUrl;
    private String dbUsername;
    private String dbPassword;
    private String index;
    private String type;
    private String river;

    public String getDbJdbcUrl() {
        return dbJdbcUrl;
    }

    public void setDbJdbcUrl(String dbJdbcUrl) {
        this.dbJdbcUrl = dbJdbcUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRiver() {
        return river;
    }

    public void setRiver(String river) {
        this.river = river;
    }
}
