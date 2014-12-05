package bynull.realty.config;

/**
 * @author dionis on 03/12/14.
 */
public class Config {
    private String vkRedirectURL;
    private String s3Folder;

    public String getVkRedirectURL() {
        return vkRedirectURL;
    }

    public void setVkRedirectURL(String vkRedirectURL) {
        this.vkRedirectURL = vkRedirectURL;
    }

    public String getS3Folder() {
        return s3Folder;
    }

    public void setS3Folder(String s3Folder) {
        this.s3Folder = s3Folder;
    }
}
