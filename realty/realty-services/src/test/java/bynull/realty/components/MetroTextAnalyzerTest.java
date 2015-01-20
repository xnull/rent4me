package bynull.realty.components;

import bynull.realty.components.text.MetroTextAnalyzer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MetroTextAnalyzerTest {
    MetroTextAnalyzer analyzer;

    @Before
    public void setUp() throws Exception {
        analyzer = new MetroTextAnalyzer();
        analyzer.afterPropertiesSet();
    }

    @Test
    public void simpleSuccessCases() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро " + metroName, metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро ул. Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м. Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м. ул. Старокачаловской", metroName), is(true));
    }

    @Test
    public void successCaseWithMultipleOccurrencesOfTargetWordsCases() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("На ул. Матроскиных сдается 2-ух комнатная квартира недалеко от станции метро ул. Старокачаловской", metroName), is(true));
    }

    @Test
    public void successCaseКитайГород() {
        String text = "ЖИЛЬЕ: Самый ЦЕНТР Москвы от 500 рублей в сутки! 9 минут пешком от Кремля! Комната для девушек. Комната для ребят. ЗВОНИТЕ \uD83D\uDCDE -84995048897 И БРОНИРУЙТЕ) На месяц и более. Выберите Ваш тариф: Тариф \"Standart\": 4 и 6м - 500 рублей в сутки с человека, Тариф \"Comfort\": 4 и 6м - 700 рублей в сутки с чел(все включено) Оптом ДЕШЕВЛЕ) Обычная цена 800-1000 рублей! АКЦИЯ до 1 февраля! ГАРАНТИРУЕМ: Вы выспитесь, Вы отдохнете, Вы сэкономите время и деньги, Вы сохраните здоровье и нервы! 3 минуты пешком или 1 минута бегом от м.Китай город и Лубянка) Вы получите БЕСПЛАТНО: чай, кофе, сахар, wi fi, сейф, стирка, смена белья/полотенце, парковка во дворе. Уважение и любовь, тишина и спокойствие, здоровый сон, комфорт и чистота, личная безопасность , практика иностранных языков. Поможем Вам с тяжелыми чемоданами) Фейс-контроль, РФ, славяне ЗВОНИТЕ \uD83D\uDCDE-84995048897 И БРОНИРУЙТЕ МЕСТО) Будем Вам РАДЫ) АКЦИЯ до 1 февраля! ";

        assertThat(analyzer.matches(text, "Китай город"), is(true));
        assertThat(analyzer.matches(text, "Лубянка"), is(true));
    }

    @Test
    public void failWordsAreInDifferentOrder() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("Сдается квартира на улице Пушкина. Недалеко от магазина Старокачаловская. Метро Васечкина неподалеку.", metroName), is(false));
    }

    @Test
    public void failWordsAreInDifferentOrder2() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("Сдается квартира на улице Пушкина. Метро Васечкина неподалеку. Недалеко от магазина Старокачаловская.", metroName), is(false));
    }

    @Test
    public void failDistanceBetweenWordsAreTooGreat() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("Сдается квартира. Недалеко от метро Васечкина и магазина Петровича. " + metroName + ".", metroName), is(false));
    }

    @Test
    public void fix() {
        String text = "РУССКАЯ СЕМЬЯ ИЗ 3х ЧЕЛОВЕК СНИМЕТ 2x КОМНАТНУЮ КВАРТИРУ В РАЙОНЕ М.ДОСТОЕВСКАЯ, Ул.ГИЛЯРОВСКОГО, ОКТЯБРЬСКАЯ +7 926 089 63 01";
        assertThat(analyzer.matches(text, "Улица Старокачаловская"), is(false));
        assertThat(analyzer.matches(text, "Достоевская"), is(true));
        assertThat(analyzer.matches(text, "Улица Скобелевская"), is(false));
        assertThat(analyzer.matches(text, "Улица Академика Янгеля"), is(false));
        assertThat(analyzer.matches(text, "Улица Подбельского"), is(false));
        assertThat(analyzer.matches(text, "Улица Горчакова"), is(false));
        assertThat(analyzer.matches(text, "Улица 1905 года"), is(false));
    }

    @Test
    public void fix2() {
        String text = "Сдам квартиру 3-комн. квартиру. (56 м2) В двушке отделили перегородкой часть комнаты, получилась отдельная проходная комната. 15 мин. от метро ул. Скобелевская, в новом доме. Ремонт от застройщика, два застекленных балкона, Комната 17,5 с балконом и 2 смежные: 17,5 м и 12 м. Мебель есть, холодильник, эл.плита, стиралка. Все для жизни есть, в рабочем состоянии. Собственник не появляется, деньги на карту отправляли. Залог за последний месяц обязателен при въезде. Без комиссии! с\\у раздельный, стиральная машина автомат, холодильник, железная дверь,консьерж. НЕ АГЕНТСТВО!!! Комиссии агентов нет - собираемся съезжать, ищем замену! Стоимость аренды - 42 000 тысячи рублей, включая к.у.+ Залог за последний месяц. Договор с собственником возможно заключить. Заезд на следующий день! ";
        assertThat(analyzer.matches(text, "Улица Скобелевская"), is(true));
    }
}