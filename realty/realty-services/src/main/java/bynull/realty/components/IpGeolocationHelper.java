package bynull.realty.components;

import bynull.realty.data.common.GeoPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author dionis
 *         19/10/15.
 */
public class IpGeolocationHelper {
    private static final int MAX_ALLOWED_REQUESTS_PER_MINUTE = 250;
    private final Map<String, GeoPoint> cache = new HashMap<>();
    private final Map<Long, Integer> mapToRequestCount = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private IpGeolocationHelper() {
    }

    private static final IpGeolocationHelper INSTANCE = new IpGeolocationHelper();

    public static IpGeolocationHelper getInstance() {
        return INSTANCE;
    }

    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};




    public Optional<GeoPoint> findGeoLocationByIp(String ip) {
        synchronized (cache) {
            GeoPoint cachedResult = cache.get(ip);
            if (cachedResult != null) {
                return Optional.of(cachedResult);
            }
        }

        synchronized (mapToRequestCount) {
            long minutesSince1970 = System.currentTimeMillis() / (1000 * 60);
            Integer count = mapToRequestCount.get(minutesSince1970);
            if (count == null) {
                mapToRequestCount.put(minutesSince1970, 1);
            } else {
                if(count == MAX_ALLOWED_REQUESTS_PER_MINUTE) {
                    return Optional.empty();
                }
                mapToRequestCount.put(minutesSince1970, 1 + count);
            }
        }


            GetMethod method = new GetMethod("http://ip-api.com/json/" + ip);
            try {
                int responseCode = httpManager.executeMethod(method);
                if(responseCode == 200) {
                    String responseBodyAsString = method.getResponseBodyAsString();
                    Map map = objectMapper.readValue(responseBodyAsString, Map.class);
                    String status = (String) map.get("status");
                    if("success".equalsIgnoreCase(status)) {
                        double lat = Double.valueOf(String.valueOf(map.get("lat")));
                        double lng = Double.valueOf(String.valueOf(map.get("lon")));
                        GeoPoint geoPoint = new GeoPoint().withLatitude(lat).withLongitude(lng);
                        synchronized (cache) {
                            cache.put(ip, geoPoint);
                        }
                        return Optional.of(geoPoint);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            } finally {
                method.releaseConnection();
            }
            return Optional.empty();

    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Optional<GeoPoint> geoLocationByIp = IpGeolocationHelper.getInstance().findGeoLocationByIp("87.119.186.33");
        long end = System.currentTimeMillis();
        System.out.println(geoLocationByIp);
        System.out.println("Executed in: "+(end - start)+" ms");
    }
}
