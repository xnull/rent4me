package bynull.realty.components.text;

import bynull.realty.common.PhoneUtil;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PhoneNumberParserTest {
    PhoneNumberParser parser;

    @Before
    public void setUp() {
        parser = PhoneNumberParser.getInstance();
    }

    @Test
    public void hasPhoneNumberTest() throws Exception {
        assertThat(parser.hasPhoneNumber("бла бла бла телефон 8 916 995 54 54 asddad"), is(true));
    }

    @Test
    public void parsePhoneNumbers() throws Exception {
        String text = "Сдаю срочно 1-комн.квартиру. " +
                "Москва ул.Силигерская 26к1 (12 мин на марш. до метро Петр-Разумовская)." +
                "Общ.пл.39кв.м. комната 19кв.м,кухня 9кв.м. Новый дом, отличная планировка. " +
                "Отделка от застройщика. Аренда 30 000 + счетчики. БЕЗ ДЕПОЗИТА. 8 916 995 54 54\n";
        List<PhoneUtil.Phone> phoneNumbers = parser.findPhoneNumbers(text);

        PhoneUtil.Phone first = Iterables.getFirst(phoneNumbers, null);
        assertThat(first, is(notNullValue()));
        assertThat(first, is(notNullValue()));
        assertThat(first.national, is("8 (916) 995-54-54"));
    }

    @Test
    public void testFindFirstPhoneNumber() throws Exception {
        assertEquals("8 (926) 145-10-07", PhoneUtil.findFirstPhoneNumberSafe("89261451007", "RU").getPhoneNumber().get());
        assertEquals("8 (926) 084-57-09", PhoneUtil.findFirstPhoneNumberSafe("+79260845709", "RU").getPhoneNumber().get());
        assertEquals("8 (911) 839-49-44", PhoneUtil.findFirstPhoneNumberSafe("+7 911 839 49 44", "RU").getPhoneNumber().get());
        assertEquals("8 (408) 800-88-21", PhoneUtil.findFirstPhoneNumberSafe("4088008821", "RU").getPhoneNumber().get());
        assertEquals("8 (915) 471-28-71", PhoneUtil.findFirstPhoneNumberSafe("8-915-471-28-71", "RU").getPhoneNumber().get());
        assertEquals(Optional.of("123"), PhoneUtil.findFirstPhoneNumberSafe("123", "RU").getPhoneNumber());
    }

}