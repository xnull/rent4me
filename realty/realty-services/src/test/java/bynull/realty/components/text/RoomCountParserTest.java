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
        assertThat(parser.getRoomNumber("отличная однокомнатная квартира"), is(1));
        assertThat(parser.getRoomNumber("отличную однокомнатную квартиру"), is(1));
        assertThat(parser.getRoomNumber("однушка"), is(1));
        assertThat(parser.getRoomNumber("однокомнатная"), is(1));
        assertThat(parser.getRoomNumber("одно-комнатная"), is(1));
        assertThat(parser.getRoomNumber("1-комнатная"), is(1));
        assertThat(parser.getRoomNumber("1-ушка"), is(1));
        assertThat(parser.getRoomNumber("1-к бла квартира"), is(1));
        assertThat(parser.getRoomNumber("1к бла кв"), is(1));
    }

    @Test
    public void parseTwoRoom() {
        assertThat(parser.getRoomNumber("отличная двухкомнатная квартира"), is(2));
        assertThat(parser.getRoomNumber("отличная двукомнатная квартира"), is(2));
        assertThat(parser.getRoomNumber("отличную двухкомнатную квартиру"), is(2));
        assertThat(parser.getRoomNumber("отличную двукомнатную квартиру"), is(2));
        assertThat(parser.getRoomNumber("двушка"), is(2));
        assertThat(parser.getRoomNumber("двукомнатная"), is(2));
        assertThat(parser.getRoomNumber("двухкомнатная"), is(2));
        assertThat(parser.getRoomNumber("двух комнатная"), is(2));
        assertThat(parser.getRoomNumber("дву комнатная"), is(2));
        assertThat(parser.getRoomNumber("две комнатная"), is(2));
        assertThat(parser.getRoomNumber("две комнаты"), is(2));
        assertThat(parser.getRoomNumber("дву-комнатная"), is(2));
        assertThat(parser.getRoomNumber("2-комнатная"), is(2));
        assertThat(parser.getRoomNumber("2-ушка"), is(2));
        assertThat(parser.getRoomNumber("2-к бла квартира"), is(2));
        assertThat(parser.getRoomNumber("2к бла кв"), is(2));
    }

    @Test
    public void parseThreeRoom() {
        assertThat(parser.getRoomNumber("отличная трехкомнатная квартира"), is(3));
        assertThat(parser.getRoomNumber("отличная трекомнатная квартира"), is(3));
        assertThat(parser.getRoomNumber("отличную трехкомнатную квартиру"), is(3));
        assertThat(parser.getRoomNumber("отличную трёхкомнатную квартиру"), is(3));
        assertThat(parser.getRoomNumber("отличную трикомнатную квартиру"), is(3));
        assertThat(parser.getRoomNumber("трешка"), is(3));
        assertThat(parser.getRoomNumber("трёшка"), is(3));
        assertThat(parser.getRoomNumber("трехкомнатная"), is(3));
        assertThat(parser.getRoomNumber("трёхкомнатная"), is(3));
        assertThat(parser.getRoomNumber("трех комнатная"), is(3));
        assertThat(parser.getRoomNumber("три комнатная"), is(3));
        assertThat(parser.getRoomNumber("трех комнатная"), is(3));
        assertThat(parser.getRoomNumber("трёх комнатная"), is(3));
        assertThat(parser.getRoomNumber("три комнаты"), is(3));
        assertThat(parser.getRoomNumber("трех-комнатная"), is(3));
        assertThat(parser.getRoomNumber("трёх-комнатная"), is(3));
        assertThat(parser.getRoomNumber("три-комнатная"), is(3));
        assertThat(parser.getRoomNumber("3-комнатная"), is(3));
        assertThat(parser.getRoomNumber("3-ушка"), is(3));
        assertThat(parser.getRoomNumber("3-ешка"), is(3));
        assertThat(parser.getRoomNumber("3-ёшка"), is(3));
        assertThat(parser.getRoomNumber("3ёшка"), is(3));
        assertThat(parser.getRoomNumber("3-шка"), is(3));
        assertThat(parser.getRoomNumber("3шка"), is(3));
        assertThat(parser.getRoomNumber("3-к бла квартира"), is(3));
        assertThat(parser.getRoomNumber("3к бла кв"), is(3));
    }
}