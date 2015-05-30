package bynull.realty.components.text;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UrlParserTest {
    UrlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = UrlParser.getInstance();
    }


    @Test
    public void findUrls_Simple() throws Exception {
        String text = "http://www.ib-bank.ru/news/3868";

        Set<String> urls = parser.findUrls(text);
        assertThat(urls.size(), is(1));
        assertThat(urls.contains("http://www.ib-bank.ru/news/3868"), is(true));
    }

    @Test
    public void findOneUrl() throws Exception {
        String text = "Ответ русских на блокировку SWIFT: мало не покажется<br/><br/>Уже больше года под предлогом кризиса на\n" +
                "            Украине Россию угрожают отключить от сервиса SWIFT, который сегодня позволяет 600 российским банкам и\n" +
                "            организациям свободно проводить платежи с тысячами банковских и финансовых организаций из 209 стран. Ответ\n" +
                "            Банка России слабым не покажется: уже в мае 2015 года будет запущен российский аналог SWIFT...<br/><br/>Читать\n" +
                "            далее:<br/><br/>http://www.ib-bank.ru/news/3868";

        Set<String> urls = parser.findUrls(text);
        assertThat(urls.size(), is(1));
        assertThat(urls.contains("http://www.ib-bank.ru/news/3868"), is(true));
    }

    @Test
    public void findMultipleUrls() throws Exception {
        String text = "Ответ русских на блокировку SWIFT: мало не покажется<br/><br/>Уже больше года под предлогом кризиса на\n" +
                "            Украине Россию угрожают отключить от сервиса SWIFT, который сегодня позволяет 600 российским банкам и\n" +
                "            организациям свободно проводить платежи с тысячами банковских и финансовых организаций из 209 стран. Ответ\n" +
                "            Банка России слабым не покажется: уже в мае 2015 года будет запущен российский аналог SWIFT...<br/><br/>Читать\n" +
                "            далее:<br/><br/>http://www.ib-bank.ru/news/3868,http://www.ib-bank.ru/news/3869";

        Set<String> urls = parser.findUrls(text);
        assertThat(urls.size(), is(2));
        assertThat(urls.contains("http://www.ib-bank.ru/news/3868"), is(true));
        assertThat(urls.contains("http://www.ib-bank.ru/news/3869"), is(true));
    }
}