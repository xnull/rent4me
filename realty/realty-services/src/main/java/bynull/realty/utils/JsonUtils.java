package bynull.realty.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public final class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);

    private final ObjectMapper mapper;

    /**
     * Jaxb annotations support: https://github.com/FasterXML/jackson-module-jaxb-annotations
     */
    private JsonUtils() {
        mapper = new ObjectMapper();
        //mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //Pretty print
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);
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

    public static void saveToFile(File file, Object bean) throws JsonMapperException {
        try {
            JsonUtilsHolder.INSTANCE.mapper.writeValue(file, bean);
        } catch (IOException e) {
            throw new JsonMapperException("Error mapping");
        }
    }

    public static <T> T loadFromFile(File file, Class<T> bean) throws JsonMapperException {
        try {
            return JsonUtilsHolder.INSTANCE.mapper.readValue(file, bean);
        } catch (IOException e) {
            throw new JsonMapperException("Error mapping");
        }
    }

    private static class JsonUtilsHolder {
        private static final JsonUtils INSTANCE = new JsonUtils();
    }
}