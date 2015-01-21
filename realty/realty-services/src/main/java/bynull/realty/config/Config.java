package bynull.realty.config;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;

/**
 * @author dionis on 03/12/14.
 */
@Getter
@Setter
public class Config {
    private String vkRedirectURL;
    private String fbAppId;
    private String fbSecret;
    private String vkAppId;

    private String s3Folder;
    @Resource
    private ESConfig esConfig;


}
