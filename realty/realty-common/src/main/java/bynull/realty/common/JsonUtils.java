package bynull.realty.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utils is needed for json mapping
 * Created by Vyacheslav Petc (v.pets@oorraa.net) on 18.09.14.
 */
@Slf4j
public class JsonUtils {
    public static final ObjectMapper MAPPER;
    public static final ObjectMapper PRETTY_JSON_MAPPER;

    static {
        MAPPER = buildMapper(false);
        PRETTY_JSON_MAPPER = buildMapper(true);
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * Jaxb annotations: https://github.com/FasterXML/jackson-module-jaxb-annotations
     */
    public static ObjectMapper buildMapper(boolean prettyPrint) {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        if (prettyPrint) {
            //Pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        JaxbAnnotationModule module = new JaxbAnnotationModule();
        mapper.registerModule(module);

        return mapper;
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonMapperException {
        log.trace("From json: {}", json);

        if (json == null) {
            return null;
        }

        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new JsonMapperException(e);
        }
    }

    public static <T> T fromJson(InputStream input, Class<T> type) throws JsonMapperException {
        if (input == null) {
            return null;
        }
        try {
            return MAPPER.readValue(input, type);
        } catch (Exception e) {
            throw new JsonMapperException(e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonMapperException {
        log.trace("From json: {}", json);

        if (json == null) {
            return null;
        }

        try {
            return MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonMapperException(e);
        }
    }

    public static <T> String toJson(T bean) throws JsonMapperException {
        try {
            return MAPPER.writeValueAsString(bean);
        } catch (IOException e) {
            throw new JsonMapperException(e);
        }
    }

    public static <T> String toPrettyJson(T bean) throws JsonMapperException {
        try {
            return PRETTY_JSON_MAPPER.writeValueAsString(bean);
        } catch (IOException e) {
            throw new JsonMapperException(e);
        }
    }

    public static <T> void saveToFile(File file, T bean) throws JsonMapperException {
        try {
            MAPPER.writeValue(file, bean);
        } catch (IOException e) {
            throw new JsonMapperException("Error mapping", e);
        }
    }

    public static <T> T loadFromFile(File file, Class<T> bean) throws JsonMapperException {
        try {
            return MAPPER.readValue(file, bean);
        } catch (IOException e) {
            throw new JsonMapperException("Error mapping", e);
        }
    }


    public static class JsonMapperException extends Exception {

        public JsonMapperException(String message) {
            super(message);
        }

        public JsonMapperException(String message, Throwable cause) {
            super(message, cause);
        }

        public JsonMapperException(Throwable cause) {
            super(cause);
        }

        public JsonMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
