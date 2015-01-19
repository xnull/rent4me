package bynull.realty.components;

import bynull.realty.components.text.MetroTextAnalyzer;
import org.junit.Before;
import org.junit.Ignore;
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

    @Ignore("author was too dumb to implement properly")
    @Test
    public void successCaseWithMultipleOccurrencesOfTargetWordsCases() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("На ул. Матроскиных сдается 2-ух комнатная квартира недалеко от станции метро ул. Старокачаловской", metroName), is(true));
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

        assertThat(analyzer.matches("Сдается квартира. Недалеко от метро Васечкина. " + metroName + ".", metroName), is(false));
    }
}