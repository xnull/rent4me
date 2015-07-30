package bynull.realty.converters.apartments;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by null on 7/30/15.
 */
public class FacebookApartmentModelDTOConverterTest {

    @Test
    public void testGetAuthorId() throws Exception {
        String link = "https://www.facebook.com/131345607204302";
        assertEquals("131345607204302", FacebookApartmentModelDTOConverter.parseAuthorId(link));
    }
}