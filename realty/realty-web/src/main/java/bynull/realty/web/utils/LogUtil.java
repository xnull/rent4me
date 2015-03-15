package bynull.realty.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Created by dionis on 3/15/15.
 */
public class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public static void initRequestContext() {
        setRequestId(UUID.randomUUID().toString());
    }

    public static String getRequestId() {
        return MDC.get(Constants.LOGGING_REQUEST_CONTEXT);
    }

    public static boolean setRequestId(String requestId) {
        if(requestId == null){
            LOGGER.warn("Request id not passed. Generating new one");
            requestId = UUID.randomUUID().toString();
        }
        String formerRequestId = getRequestId();
        if (formerRequestId == null) {
            MDC.put(Constants.LOGGING_REQUEST_CONTEXT, requestId);
            return true;
        } else {
            return false;
        }
    }

    public static void clearRequestContext() {
        MDC.remove(Constants.LOGGING_REQUEST_CONTEXT);
    }
}
