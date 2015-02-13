package bynull.realty.components.text;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
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
        String text = "Всем привет !!! Я .мой друг и интеллигентный кот ищем двухкомнатную квартиру " +
                "(комнаты изолированные) в шаговой доступности от метро на длительный срок 12. 40-45 000 Бюджет. ";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("45000")));
    }

    @Test
    public void fullPriceAbove_1000() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. " +
                "1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок. " +
                "33000 + залог + свет 89258605273 ";
        RentalFeeParser.PatternCheck patternCheck = parser.fullPriceAbove_1000;
        Pattern pattern = patternCheck.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup)), is("33000"));
    }

    @Test
    public void fullPriceAbove_1000_EndOfLine() {
        String text = parser.normalizeText("Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок 89258605273 . 33000 ");
        RentalFeeParser.PatternCheck patternCheck = parser.fullPriceAbove_1000;
        Pattern pattern = patternCheck.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup)), is("33000"));
    }

    @Test
    public void fullPriceBellow_1000() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок. 330 + залог + свет 89258605273 ";
        RentalFeeParser.PatternCheck patternCheck = parser.fullPriceBellow_1000;
        Pattern pattern = patternCheck.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup)), is("330"));
    }

    @Test
    public void fullPriceBellow_1000_EndOfLine() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. 1 этаж, состояние хорошее, мебель и бытовая техника есть, сдается на длительный срок 89258605273 . 330 ";
        RentalFeeParser.PatternCheck patternCheck = parser.fullPriceBellow_1000;
        Pattern pattern = patternCheck.pattern;
        Matcher matcher = pattern.matcher(text);
        assertThat(matcher.matches(), is(true));
        assertThat(StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup)), is("330"));
    }

    @Test
    public void fixForPhoneNumber() {
        String text = "Сдам 1 ком квартиру м.Аэроморт, проезд Аэропорта 6, пешком. " +
                "1 этаж, состояние хорошее, мебель и бытовая техника есть, " +
                "сдается на длительный срок. 33000 + залог + свет 89258605273 ";
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

    @Test
    public void fixAgressiveThousandsAbbreviation() {
        String text = "[СДАМ] Сдаем на длительный срок однокомнатную квартиру 38 м2 в современном монолитно-кирпичном доме 2010 года постройки, с хорошей придомовой территорией.Собственник. Квартира сдается с ремонтом от застройщика. Есть все необходимое для жизни- мебель и бытовая техника.Балкон застеклен. Санузел раздельный.Отличный чистый подъезд с консьержем. Вся инфраструктура рядом. Тихий зеленый двор с детской площадкой. 5 минут общественным транспортом до м.Речной вокзал.Метро на ул.Дыбенко в 2015 году. Адрес :ул.Петрозаводская д.24 к.2Желательно семейным парам,можно с маленькими детьми. 30 т.р.+электроэнергия+консьерж.депозит можно разбить на 2 месяца. https://www.facebook.com/profile.php?id=100008319820599&fref=nf \t1 \t2015 \thttps://www.facebook.com/profile.php?id=100008319820599 \tkvartira.msk \t\n" +
                "\n" +
                "    Речной вокзал\n" +
                "\n" +
                "\tMon Jan 26 07:22:00 EST 2015 \tMon Jan 26 07:22:00 EST 2015 \t\n";

        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("30000")));
    }


    @Test
    public void fixBugSimplePatternWithCurrencyAbove1000_1() throws Exception {
        final String text = "Сдам однокомнатную квартиру на неограниченное время. " +
                "Отличная квартира. Героев панфиловцев 22 корп 2, 38 кв. м. Квартира премиум-класса " +
                "с качественным ремонтом и евроотделкой. Гостиная обьедененна с кухней. " +
                "Кухня полностью оборудована техникой, " +
                "Кухня полностью укомплектована, есть вся необходимая мебель и техника: на кухне гарнитур, " +
                "обеденный стол, стулья, холодильник, газовая печь, СВЧ, электрический чайник. " +
                "Хорошая мебель. Имеется лоджия. цифровое TV, удобная транспортная доступность. " +
                "Арендная плата 39000 руб.! Планерная, улица Героев панфиловцев 22 корп 2, кол. кв. м. 38 " +
                "Оплата в месяц 39000 руб. 8(967) 212 74 73";

        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("39000")));
    }

    @Test
    public void fixBugSimplePatternWithCurrencyAbove1000_2_0() throws Exception {
        final String text = "Субаренда без комиссии! " +
                "Сдам в аренду комнату в двухкомнатной квартире с качественным ЕВРОРЕМОНТНОМ." +
                "Вся необходимая мебель и бытовая техника для комфортного проживания.В квартире проживает один мужчина 31г ." +
                "Рассмотрю СТРОГО ОДНОГО человека! от 25 до 35 лет с гражданством РФ,славянской внешности." +
                "В комнате двухспальная кровать,прикроватная тумба,шкаф угловой с зеркальными дверями,комод,кондиционер," +
                "ТВ плазма 32,НТВ+,балкон объединенный с комнатой, сушилка для вещей.Квартира находится в 15 минутах пешком " +
                "от станции Железнодорожная. тел. 8 964 770 73 86 Звонить с 9.00 до 23.00";

        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

    @Test
    public void fixBugSimplePatternWithCurrencyAbove1000_2_1() throws Exception {
        final String text = "Субаренда без комиссии! " +
                "Сдам в аренду комнату в двухкомнатной квартире с качественным ЕВРОРЕМОНТНОМ." +
                "Вся необходимая мебель и бытовая техника для комфортного проживания.В квартире проживает один мужчина 31г ." +
                "Рассмотрю СТРОГО ОДНОГО человека! от 25 до 35 лет с гражданством РФ,славянской внешности." +
                "В комнате двухспальная кровать,прикроватная тумба,шкаф угловой с зеркальными дверями,комод,кондиционер," +
                "ТВ плазма 32,НТВ+,балкон объединенный с комнатой, сушилка для вещей.Квартира находится в 15 минутах пешком " +
                "от станции Железнодорожная. тел. 8 964 770 73 86 тел. 8 965 398-52-65 Звонить с 9.00 до 23.00";

        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

    @Test
    public void fixBugSimplePatternWithCurrencyAbove1000_2_2() throws Exception {
        final String text = "Субаренда без комиссии! " +
                "Сдам в аренду комнату в двухкомнатной квартире с качественным ЕВРОРЕМОНТНОМ." +
                "Вся необходимая мебель и бытовая техника для комфортного проживания.В квартире проживает один мужчина 31г ." +
                "Рассмотрю СТРОГО ОДНОГО человека! от 25 до 35 лет с гражданством РФ,славянской внешности." +
                "В комнате двухспальная кровать,прикроватная тумба,шкаф угловой с зеркальными дверями,комод,кондиционер," +
                "ТВ плазма 32,НТВ+,балкон объединенный с комнатой, сушилка для вещей.Квартира находится в 15 минутах пешком " +
                "от станции Железнодорожная. тел. 8 964 770 73 86 тел. 8 965 398-52-65 тел. 8 964 770 73 87 тел. 8 965 398-52-66 Звонить с 9.00 до 23.00";

        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

    @Test
    public void fixBugSimplePatternWithCurrencyAbove1000_3() throws Exception {
        final String text = "Сдам однокомнатную квартиру чистоплотным русским без домашних питомцев " +
                "на не менее чем на 1 месяц! Бунинская аллея, улица Кадырова ул д. 8, корп. 1, кв. м. 31 " +
                "Оплата в месяц 30000 руб.! 8(967) 212 74 70";

        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("30000")));
    }

    @Test
    public void fixBugSimplePatternWithCurrencyBelow1000() throws Exception {
        final String text = "Сдам однокомнатную квартиру на неограниченное время. " +
                "Отличная квартира. Героев панфиловцев 22 корп 2, 38 кв. м. Квартира премиум-класса " +
                "с качественным ремонтом и евроотделкой. Гостиная обьедененна с кухней. " +
                "Кухня полностью оборудована техникой, " +
                "Кухня полностью укомплектована, есть вся необходимая мебель и техника: на кухне гарнитур, " +
                "обеденный стол, стулья, холодильник, газовая печь, СВЧ, электрический чайник. " +
                "Хорошая мебель. Имеется лоджия. цифровое TV, удобная транспортная доступность. " +
                "Арендная плата 390 руб.! Планерная, улица Героев панфиловцев 22 корп 2, кол. кв. м. 38 " +
                "Оплата в месяц 390 руб. 8(967) 212 74 73";

        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("390")));
    }

    @Test
    public void fixBug_58() throws Exception {
        final String text = "СРОЧНО Сдам МЕСТО в комнате для парня (на подселение)  БЕЗ КОМИССИИ! Я НЕ АГЕНТ!\n" +
                "\n" +
                "Москва, м. Печатники, 10-15 мин. пешком до метро  (5 мин на автобусе без пробок). \n" +
                "\n" +
                "Преимущества: + Проживание без хозяев + Большая, светлая 1-к. квартира с чудным видом из окна! " +
                "+ Ищем строго: спокойных, адекватных, платежеспособных, чистоплотных, непьющих, некурящих парней. " +
                "Возраст до 30 лет. Желательно, кто работает весь день. " +
                "+ Ищем на длит. срок => нет постоянной текучки => сохранность ваших вещей " +
                "+ Нет ограничений по времени прихода/ухода (У каждого свой ключ) " +
                "+ Соблюдаем чистоту, тишину и порядок (Уборка каждую неделю) " +
                "+ Есть все необходимое для проживания: Удобный район, магазины, набережная, в квартире: " +
                "Мебель, Wi-Fi, Холодильник, Посуда, Стиральная машина, Микроволновка, Большая лоджия, Металл. дверь. " +
                " + Близко до центра города (20 мин. на метро до Арбата!) + Автобус от метро ходит до часу ночи, бес пробок. " +
                "+ В соседнем дворе есть поликлиника + Невысокая Оплата: 8000 руб. в мес (включая коммуналку) + депозит 4000 руб.\n" +
                "\n" +
                "Торопитесь, хорошие места сдаются быстро!!!  Звонить: 8926-143-46-03 Дмитрий";

        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("8000")));
    }

    @Test
    public void fixBug_64_comment_1() throws Exception {
        final String text = "Срочно сдам койко-место или комнату по койко-местам (6 коек) " +
                "в 2-х комн. кв., м.Щукинская, 4 мин. пеш., в квартире косметический ремонт, " +
                "чисто, вся мебель, стиральная машинка, интернет проведен, в соседней комнате добродушная женщина, " +
                "рассмотрим всех желающих, 5500 + 2500 р. первый месяц, потом каждый месяц по 5500 р. (все включено), " +
                "Звонить: 8 916 900 15 54 Максим";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("5500")));

    }

    @Test
    public void fixBug_64_comment_2() throws Exception {
        final String text = "Срочно сдам уютную комнатку в 2 - х комн. кв. (блочного типа), м. Перово 10 мин. пеш., " +
                "в соседях две добрые девушки (русские), в квартире чисто, легкий косметический ремонт, " +
                "В комнате двухспальная кровать, новый огромный вместительный шкаф, телевизор, стол и т. п., " +
                "рассмотрим славян, желательно пару, ПРОСМОТР РАННИМ УТРОМ! " +
                "Без депозита и Залога, 16 000 р. Звонить: 8 916 900 15 54";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("16000")));

    }


    @Test
    public void fixBug_64_comment_3() throws Exception {
        final String text = "Сдам комнату в 2-х комнатной квартире,желательно девушке," +
                "можно 2-м девушкам( или семейной паре, главное порядочным) 700 метров до м.Лермонтовский проспект." +
                "В комнате 2-х спальная кровать,комп. стол,шкаф,wifi,чистая уютная квартира,цена 16т. " +
                "Все вопросы в л.с.,звоните,пишите,отвечу всем,рассмотрю все пожелания и предложения) " +
                "я не агент,не хулиган и не зануда))";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("16000")));
    }
    @Test
    public void fixBug_64_comment_3_end_of_string() throws Exception {
        final String text = "Сдам комнату в 2-х комнатной квартире,желательно девушке," +
                "можно 2-м девушкам( или семейной паре, главное порядочным) 700 метров до м.Лермонтовский проспект." +
                "Все вопросы в л.с.,звоните,пишите,отвечу всем,рассмотрю все пожелания и предложения) " +
                "я не агент,не хулиган и не зануда)) "+
                "В комнате 2-х спальная кровать,комп. стол,шкаф,wifi,чистая уютная квартира,цена 16т.";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("16000")));
    }

    @Test
    public void fixBug_64_comment_4() throws Exception {
        final String text = "СДАМ 1-КОМНАТНУЮ КВАРТИРУ. 15000 РУБ В МЕСЯЦ" +
                " + счетчики г. Москва, пос. Десеновское (Новые Ватутинки) Подробности - в личных сообщениях.\n" +
                "\n" +
                "http://maps.yandex.ru/?z=14&pt=37.348329,55.518520&ll=37.348329,55.518520";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("15000")));
    }


    @Test
    public void fixBug_64_comment_5() throws Exception {
        final String text = "Сдам комнату с балконом в 2-х комн. кв., м. Жулебино, 6 мин. пеш., в соседях мать и сын, " +
                "арендаторы, славяне, в квартире чисто, уютно, есть вся мебель, два дивана, шкаф, стиральная машинка, " +
                "рассмотрим только девушек, можно 2-х. " +
                "Оплата: первый месяц 15000 р.+ 7000 р. последующие по 15000 р. " +
                "БОЛЬШЕ НИ ЗА ЧТО ПЛАТИТЬ НЕ НАДО!!! Звонить:8 916 661 12 29 Максим";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("15000")));
    }

    @Test
    public void fixBug_64_comment_6() throws Exception {
        final String text = "Сдаю срочно 1-комн.квартиру. Москва ул.Силигерская 26к1 " +
                "(12 мин на марш. до метро Петр-Разумовская)." +
                "Общ.пл.39кв.м. комната 19кв.м,кухня 9кв.м. " +
                "Новый дом, отличная планировка. Отделка от застройщика. " +
                "Аренда 30 000 + счетчики. БЕЗ ДЕПОЗИТА. 8 916 995 54 54";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("30000")));
    }

    @Test
    public void fixBug_64_comment_7() throws Exception {
        final String text = "Сдам комнату в 3-х комнатной квартире, ст. м. Кузьминки (в 3-х минутах ходьбы). " +
                "Проходная светлая комната с балконом, из мебели: шкаф, диван, телевизор, кондиционер. " +
                "На кухне: вся необходимая техника. Оплата за 1 мес. (+ депозит-100% стоимость) без комиссии. " +
                "Срочно!!! Цена 20 т.р. + к/у. тел 8 926 543 59 20";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("20000")));
    }

    @Test
    public void fixBug_64_comment_8() throws Exception {
        final String text = "СРОЧНО СДАМ КВАРТИРУ! 8 916 392 31 55. ОДНУШКА М. МЕДВЕДКОВО!!!!!! ЗВОНИТЕ!!!";
        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

    @Test
    public void fixBug_64_comment_9() throws Exception {
        final String text = "Фото реальные.Сдам большую замечательную квартиру в доме бизнес-класса, в непосредственной близости от МКАД в новом доме." +
                "Неподалеку метро Новогиреево.Огороженная территория,охрана.\n" +
                "\n" +
                "В квартире есть все необходимое для комфортного проживания (вся мебель и техника). " +
                "Есть большая застекленная лоджия,кондиционер,гардеробная комната. Недавно сделан евроремонт.\n" +
                "\n" +
                "Сдается на длительный срок. Рассматриваем всех,кроме кавказа - был неудачный опыт.\n" +
                "\n" +
                "Коммунальные уже платежи включены в цену.38тыс. 89269813288 - Андрей";
        assertThat(parser.findRentalFee(text), equalTo(new BigDecimal("38000")));
    }

    @Test
    public void fixBug_64_comment_10() throws Exception {
        final String text = "Сдам большую замечательную квартиру в доме бизнес-класса, " +
                "в непосредственной близости от МКАД в новом доме.Неподалеку метро Новогиреево.Огороженная территория,охрана.\n" +
                "\n" +
                "В квартире есть все необходимое для комфортного проживания (вся мебель и техника). " +
                "Есть большая застекленная лоджия,кондиционер,гардеробная комната. Недавно сделан евроремонт.\n" +
                "\n" +
                "Сдается на длительный срок. Рассматриваем всех,кроме кавказа - был неудачный опыт.\n" +
                "\n" +
                "Коммунальные уже платежи включены в цену. 89269813288";
        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

    @Ignore("add test case later & fix from 'rental_fee_bugs.html'")
    @Test
    public void fixBug_64_comment_11() throws Exception {
        final String text = null;
        assertThat(parser.findRentalFee(text), is(nullValue()));
    }

}
