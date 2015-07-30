package bynull.realty.converters.apartments;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by null on 7/30/15.
 */
public class VkontakteApartmentModelDTOConverterTest {

    @Test
    public void testParseAuthorId() throws Exception {
        String link = "https://vk.com/id248324164";

        assertEquals("id248324164", VkontakteApartmentModelDTOConverter.parseAuthorId(link));

    }
}