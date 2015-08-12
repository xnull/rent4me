package bynull.realty.data.business.ids;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by null on 8/13/15.
 */
public class IdentTypeTest {

    @Test
    public void testFrom() throws Exception {
        assertEquals(IdentType.PHONE, IdentType.from(IdentType.PHONE.getType()));
    }
}