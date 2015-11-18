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
        MDC.put(Constants.LOGGING_REQUEST_CONTEXT, UUID.randomUUID().toString());
        LOGGER.info("Initialized request context");
    }

    public static void clearRequestContext() {
        MDC.remove(Constants.LOGGING_REQUEST_CONTEXT);
    }
}
