package bynull.realty.components.text;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PhoneNumberParserTest {
    PhoneNumberParser parser;

    @Before
    public void setUp() {
        parser = PhoneNumberParser.getInstance();
    }

    @Test
    public void hasPhoneNumberTest() throws Exception {
        assertThat(parser.hasPhoneNumber("бла бла бла телефон 123 456 789 10112 asddad"), is(true));
    }

}