package bynull.realty.utils;

import bynull.realty.common.JsonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by null on 7/30/15.
 */
public class JsonUtilsTest {

    @Ignore
    @Test
    public void testToJsonOptionalFields() throws Exception {
        JsonUtils.setPrettyPrint(false);

        DtoWithOptional dto = new DtoWithOptional();
        //dto.setId(Optional.of(14l));
        dto.setId(Optional.empty());

        String json = JsonUtils.toJson(dto);
        assertEquals("{}", json);

        DtoWithOptional deserialised = JsonUtils.fromJson(json, DtoWithOptional.class);
        assertNotNull(deserialised.getId());
    }

    @ToString
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    //@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class DtoWithOptional {
        @JsonProperty("id")
        //@JsonSerialize(using = JsonUtils.OptionalJsonSerializer.class)
        //@JsonDeserialize(using = JsonUtils.OptionalJsonDeserializer.class)
        private Optional<Long> id;
    }
}