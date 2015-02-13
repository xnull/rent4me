package bynull.realty.utils;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TextUtilsTest {
    @Test
    public void separateWithWhitespaceNumbersAndCharacters() throws Exception {
        String text = "a1b1";
        assertThat(TextUtils.normalizeTextAggressivelyForPriceParsing(text), is("a 1 b 1"));
    }
}