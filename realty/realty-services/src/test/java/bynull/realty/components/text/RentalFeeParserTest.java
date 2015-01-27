package bynull.realty.components.text;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class RentalFeeParserTest {
    private RentalFeeParser parser;

    @Before
    public void setUp() throws Exception {
        parser = RentalFeeParser.getInstance();
    }

    @Test
    public void simpleSuccessCases() {
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35000 р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 15 000 р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("15000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 120 000 р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("120000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35000р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35.000р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35,000р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35, 000р. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35, 000рублей. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35, 000 рублей. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35, 000 рубл. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35 000 рубл. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35000 рубл. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35.000 рубл. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35. 000 рубл. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35000 руб. (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35 000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35.000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35. 000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35,000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16 Цена — 35, 000 (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
    }

    @Test
    public void simpleInversePriceSuccessCases() {
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35000 р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 15 000 р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("15000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 120 000 р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("120000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35000р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35.000р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35,000р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35, 000р. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35, 000рублей. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35, 000 рублей. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35, 000 рубл. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35 000 рубл. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35000 рубл. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35.000 рубл. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35. 000 рубл. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35000 руб. цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35 000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35.000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35. 000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35,000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
        assertThat(parser.findRentalFee("Точный адрес квартиры — Дмитровский проезд, 16. 35, 000 цена (по нынешним временам, наверное, можно даже попробовать поторговаться)."), equalTo(new BigDecimal("35000")));
    }

    @Test
    public void successForCompleteRange() {
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру (комнаты изолированные) в шаговой доступности от метро на длительный срок. Бюджет 40 000-45 000. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("40000")));
    }

    @Test
    public void successForInverseCompleteRange() {
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру (комнаты изолированные) в шаговой доступности от метро на длительный срок 12. 40 000-45 000 Бюджет asd. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("45000")));
    }

    @Test
    public void successForInverseCompleteRange2() {
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру (комнаты изолированные) в шаговой доступности от метро на длительный срок 12. 900-1 000 Бюджет asd. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("1000")));
    }

    @Test
    public void successForIncompleteStartingRange() {
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру (комнаты изолированные) в шаговой доступности от метро на длительный срок. Бюджет 40-45 000. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("45000")));
    }

    @Test
    public void successForInverseIncompleteStartingRange() {
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру (комнаты изолированные) в шаговой доступности от метро на длительный срок 12. 40-45 000 Бюджет. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("45000")));
    }

    @Test
    public void fullPriceAbove_1000() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок. 33000 + залог + свет 89258605273 ";
        Pattern pattern = parser.fullPriceAbove_1000.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(2)), is("33000"));
    }

    @Test
    public void fullPriceAbove_1000_EndOfLine() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок 89258605273 . 33000 ";
        Pattern pattern = parser.fullPriceAbove_1000.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(2)), is("33000"));
    }

    @Test
    public void fullPriceBellow_1000() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок. 330 + залог + свет 89258605273 ";
        Pattern pattern = parser.fullPriceBellow_1000.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(2)), is("330"));
    }

    @Test
    public void fullPriceBellow_1000_EndOfLine() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок 89258605273 . 330 ";
        Pattern pattern = parser.fullPriceBellow_1000.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(2)), is("330"));
    }

    @Test
    public void fixForPhoneNumber() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок. 33000 + залог + свет 89258605273 ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("33000")));
    }

    @Test
    public void successParseForThousands() {
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 тысячи. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тысячи. "), equalTo(new BigDecimal("33000")));

        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 тыс. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тыс. "), equalTo(new BigDecimal("33000")));
    }

    @Test
    public void successParseForNonStrictThousandsAbbreviation() {
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 тыр. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 тыров "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тыров "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тыр "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тыр. "), equalTo(new BigDecimal("33000")));
    }


    @Test
    public void successParseForAggressiveThousandsAbbreviation() {
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 т. р. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 т.р. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33т.р. "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 тр "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33тр "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33т р "), equalTo(new BigDecimal("33000")));
        assertThat(parser.findRentalFee("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж. 33 т р "), equalTo(new BigDecimal("33000")));
    }
}