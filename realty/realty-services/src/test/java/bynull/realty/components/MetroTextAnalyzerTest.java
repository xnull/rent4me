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
    public void success() {
        String metroName = "Улица Старокачаловская";

        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро " + metroName, metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро ул. Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м. Улицы Старокачаловской", metroName), is(true));
        assertThat(analyzer.matches("Сдается квартира недалеко от станции м. ул. Старокачаловской", metroName), is(true));

//        assertThat(analyzer.matches("Сдается квартира недалеко от станции метро улицы Строкачаловской" + metroName, metroName), is(true));
    }
}