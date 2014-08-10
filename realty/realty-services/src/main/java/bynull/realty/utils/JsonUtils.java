package bynull.realty.utils;

import java.io.IOException;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    private static class JsonUtilsHolder {
        private static final JsonUtils INSTANCE = new JsonUtils();
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonMapperException {
        LOG.trace("From json");

        try {
            return JsonUtilsHolder.INSTANCE.mapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonMapperException(e);
        }
    }

    public static String toJson(Object bean) throws JsonMapperException {
        LOG.trace("Convert to json");

        try {
            return JsonUtilsHolder.INSTANCE.mapper.writeValueAsString(bean);
        } catch (IOException e) {
            throw new JsonMapperException(e);
        }
    }
}