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

    @Test
    public void fixNewLines() {
        String text = "[СДАМ]\n" +
                "\n" +
                "С 20 января сдается на длительный срок уютная, тихая и солнечная двухкомнатная квартира на 1-ой Фрунзенской улице. З минуты пешком от м. Фрунзенская, 4 минуты пешком от Парка Горького, развитая инфраструктура - много магазинов, рестораны и кафе, старый московский зеленый район. Квартира расположена в доме Совмина, 1 этаж, окна с решетками в очень тихий и зеленый двор, удобная парковка и выезд на основные магистрали столицы без пробок. В квартире есть все, необходимое для жизни: холодильник, стиральная машина, двуспальная кровать, диван, большой шкаф-купе и вместительная темная комната, пол - паркет, кухня, совмещенная с гостинной и отдельная спальня, с/у раздельный, ремонт не от бабушки. Идеально для пары или одиночки. \n" +
                "Цена - 60 тысяч руб в месяц, включая коммунальные платежи.Вопросы и предложения пожалуйста в личку. \n" +
                "Спасибо за репост)\n" +
                "\n" +
                "https://www.facebook.com/levrez";
        assertThat(analyzer.matches(text, "Фрунзенская"), is(true));
    }

    @Test
    public void fix4() {
        String text = "[СНИМУ]\n" +
                "\n" +
                "Молодая семья снимет 1ком-ю.квартиру. Метро Молодежная. Славянский.бульвар, Парк победы. Россияни, Москвичи.";
        assertThat(analyzer.matches(text, "Молодёжная"), is(true));
    }

    @Test
    public void fix5_MetroSymbol() {
        String text = "Самый центр Москвы! 9 минут от Кремля! Комната для 4 девушек+ одноместные кровати! Комната для 6 ребят! Ограниченное предложение! Для ценителей комфорта, безопасности , домашнего уюта! 3 минуты пешком ОТ Ⓜ Китай город и Лубянка , 9 минут до Чистых Прудов ! Адрес: Лучников переулок 7/4,стр.6, 3эт, кв.50 Месяц и более - 500 рублей в сутки с человека + депозит в счет проживания следующего месяца 5 тыс.руб (после окончания договора) Бесплатно: чай, кофе, сахар, wi fi,стиральная машинка, гладильная доска, фен, смена постельного белья, уборка, полотенце, посуда, парковка во дворе! В 3 минутах ходьбы недорогой супермаркет, банкоматы, множество кафе и тд. Гарантированно Вы получаете: быстрый интернет, обретете тишину и спокойствие, охрану и безопасность , видеонаблюдение , личные сейфы и кодовые замки на дверях, удобные кровати, практику иностранных языков. Развлечения : настольные игры , мафия, активити, а также просмотр шедевров мирового кино. Я всегда помогу Вам с тяжелыми чемоданами, достаточно только позвонить! Позвоните и забронируйте место для Вас! Ограниченное предложение! \uD83D\uDCDE- 84995048897. Ответим Вам С 7:30 до 00:00 ";
        assertThat(analyzer.matches(text, "Китай город"), is(true));
        assertThat(analyzer.matches(text, "Лубянка"), is(true));
    }

    @Test
    public void fix6_IgnoreAmpersands() {
        String text = "Сдается \"двушка\" около метро \"Октябрьского поля\". http://thelocals.ru/rooms/11884 ";
        assertThat(analyzer.matches(text, "Октябрьское поле"), is(true));
    }


}