package bynull.realty.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public final class JsonUtils {
    public static final String PRETTY_PRINT_ENV_PROP = "json.pretty";
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private final ObjectMapper mapper;

    public static void setPrettyPrint(Boolean pretty){
        System.setProperty(PRETTY_PRINT_ENV_PROP, pretty.toString());
    }

    /**
     * Jaxb annotations support: https://github.com/FasterXML/jackson-module-jaxb-annotations
     */
    private JsonUtils() {
        mapper = new ObjectMapper();
        //mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //Pretty print
        if (Boolean.valueOf(System.getProperty(PRETTY_PRINT_ENV_PROP))) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JaxbAnnotationModule());
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

    public static class OptionalJsonSerializer extends StdSerializer<Optional<?>> {
        /**
         * Create a new OptionalSerializer.
         */
        public OptionalJsonSerializer() {
            super(Optional.class, true);
        }

        @Override
        public void serialize(Optional<?> value, JsonGenerator generator, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            if (value.isPresent())
                generator.writeObject(value.get());
            else
                provider.defaultSerializeNull(generator);
        }
    }

    public static class OptionalJsonDeserializer extends StdDeserializer<Optional<?>>
            implements ContextualDeserializer {
        private static final long serialVersionUID = 1L;
        private Class<?> targetClass;

        /**
         * Creates a new OptionalDeserializer.
         */
        public OptionalJsonDeserializer() {
            super(Optional.class);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context,
                                                    BeanProperty property) throws JsonMappingException {
            if (property != null) {
                // See http://jackson-users.ning.com/forum/topics/deserialize-with-generic-type
                JavaType type = property.getType();
                JavaType ofType = type.containedType(0);
                this.targetClass = ofType.getRawClass();
            }
            return this;
        }

        @Override
        public Optional<?> deserialize(JsonParser parser, DeserializationContext context)
                throws IOException, JsonProcessingException {
            return Optional.of(parser.readValueAs(targetClass));
        }

        @Override
        public Optional<?> getNullValue() {
            return Optional.empty();
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
