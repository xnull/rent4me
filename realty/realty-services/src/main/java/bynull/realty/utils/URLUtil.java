package bynull.realty.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dionis on 06/12/14.
 */
public class URLUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(URLUtil.class);

    public static byte[] fetchExternalResourceContentOrNull(HttpClient httpClient, String url) {
        byte[] result = null;
        GetMethod getMethod = new GetMethod(url);
        try {
            getMethod.setFollowRedirects(true);
            final int responseCode = httpClient.executeMethod(getMethod);
            if(responseCode != 200) {
                result = null;
            } else {
                result = getMethod.getResponseBody();
            }
        } catch (Exception e) {
            LOGGER.warn("Exception happened while trying to load ["+url+"]", e);
            result = null;
        } finally {
            getMethod.releaseConnection();
        }
        return result;
    }
}
