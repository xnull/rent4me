package bynull.realty.components.text;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RoomCountParserTest {

    RoomCountParser parser;

    @Before
    public void setUp() throws Exception {
        parser = RoomCountParser.getInstance();
    }

    @Test
    public void parseOneRoom() {
        assertThat(parser.findRoomCount("бла бла бла бла отличная однокомнатная квартира бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла отличную однокомнатную квартиру бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла однушка бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла однокомнатная бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла одно-комнатная бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла 1-комнатная бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла 1-ушка бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла 1-к бла квартира бла бла бла бла"), is(1));
        assertThat(parser.findRoomCount("бла бла бла бла 1к бла кв бла бла бла бла"), is(1));
    }

    @Test
    public void parseTwoRoom() {
        assertThat(parser.findRoomCount("бла бла бла бла отличная двухкомнатная квартира бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла отличная двукомнатная квартира бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла отличную двухкомнатную квартиру бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла отличную двукомнатную квартиру бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла двушка бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла двукомнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла двухкомнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла двух комнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла дву комнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла две комнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла две комнаты бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла дву-комнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла 2-комнатная бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла 2-ушка бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла 2-к бла квартира бла бла бла бла"), is(2));
        assertThat(parser.findRoomCount("бла бла бла бла 2к бла кв бла бла бла бла"), is(2));
    }

    @Test
    public void parseThreeRoom() {
        assertThat(parser.findRoomCount("бла бла бла бла отличная трехкомнатная квартира бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла отличная трекомнатная квартира бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла отличную трехкомнатную квартиру бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла отличную трёхкомнатную квартиру бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла отличную трикомнатную квартиру бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трешка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трёшка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трехкомнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трёхкомнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трех комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла три комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трех комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трёх комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла три комнаты бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трех-комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла трёх-комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла три-комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-комнатная бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-ушка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-ешка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-ёшка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3ёшка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-шка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3шка бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3-к бла квартира бла бла бла бла"), is(3));
        assertThat(parser.findRoomCount("бла бла бла бла 3к бла кв бла бла бла бла"), is(3));
    }

    @Test
    public void fix3k_1() {
        String text = "Метро Ленинский проспект. Уникальное месторасположение: ЦПКиО им. Горького, «АШАН» и т.д.. \n" +
                "Предлагается койко-место в изолированной, уютной, чистой комнате (22 кв.м)., в 3-х комнатной квартире в 3 минутах пешком от м. Ленинский проспект. В комнате до 3-х человек. Рассматриваются ТОЛЬКО ДЕВУШКИ гражданства РФ, Украины, Белоруссии. В квартире имеется: мебель, односпальные кровати, микроволновка, холодильник, ТВ, утюг и бесплатный Wi-Fi.\n" +
                "В стоимость входит: коммунальные платежи, интернет, моющее средство, уборка.\n" +
                "Квартира сдается без посредников, агентов. Без комиссии и залогов!\n" +
                "Возврат остатка в случае съезда.\n" +
                "10500 р./мес.\n" +
                "89637898649";

        assertThat(parser.findRoomCount(text), is(3));
    }

    @Test
    public void fix2k_1() {
        String text = "СНИМУ 2КУ,Без посредников,в г.Щелково";

        assertThat(parser.findRoomCount(text), is(2));
    }

    @Test
    public void fix1k_1() {
        String text = "[СДАМ]\n" +
                "\n" +
                "Сдам 1к/кв. после ЕВРО РЕМОНТА метро\"Преображенская пл.\" в кв.ВСЁ НОВОЕ с/уз.раздельный-кафель до потолка ВСЯ НОВАЯ сантехника-краны,мойка,унитаз,раковины,ванна и ВСЯ НОВАЯ техника-двухкамерный холодильник,эл.плита,св-печь,стиральная машина...есть вся необходимая мебель,новый кухонный гарнитур+посуда,прихожая+шкаф купе,застеклённая лоджия,окна во двор,укреплённая мет.дверь,тёплая квартира,чистый подъезд.Семейной паре славян без животных.Депозит разделяется.Без комиссии.На очень длительный срок по договору аренды. (968)0138403 = Возможны варианты = Михаил\n" +
                "\n" +
                "https://www.facebook.com/profile.php?id=100005310012239";

        assertThat(parser.findRoomCount(text), is(1));
    }
}