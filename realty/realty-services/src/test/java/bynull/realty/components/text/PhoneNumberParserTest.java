package bynull.realty.components.text;

import bynull.realty.common.PhoneUtil;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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

}