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
        assertThat(parser.findRoomCount("отличная однокомнатная квартира"), is(1));
        assertThat(parser.findRoomCount("отличную однокомнатную квартиру"), is(1));
        assertThat(parser.findRoomCount("однушка"), is(1));
        assertThat(parser.findRoomCount("однокомнатная"), is(1));
        assertThat(parser.findRoomCount("одно-комнатная"), is(1));
        assertThat(parser.findRoomCount("1-комнатная"), is(1));
        assertThat(parser.findRoomCount("1-ушка"), is(1));
        assertThat(parser.findRoomCount("1-к бла квартира"), is(1));
        assertThat(parser.findRoomCount("1к бла кв"), is(1));
    }

    @Test
    public void parseTwoRoom() {
        assertThat(parser.findRoomCount("отличная двухкомнатная квартира"), is(2));
        assertThat(parser.findRoomCount("отличная двукомнатная квартира"), is(2));
        assertThat(parser.findRoomCount("отличную двухкомнатную квартиру"), is(2));
        assertThat(parser.findRoomCount("отличную двукомнатную квартиру"), is(2));
        assertThat(parser.findRoomCount("двушка"), is(2));
        assertThat(parser.findRoomCount("двукомнатная"), is(2));
        assertThat(parser.findRoomCount("двухкомнатная"), is(2));
        assertThat(parser.findRoomCount("двух комнатная"), is(2));
        assertThat(parser.findRoomCount("дву комнатная"), is(2));
        assertThat(parser.findRoomCount("две комнатная"), is(2));
        assertThat(parser.findRoomCount("две комнаты"), is(2));
        assertThat(parser.findRoomCount("дву-комнатная"), is(2));
        assertThat(parser.findRoomCount("2-комнатная"), is(2));
        assertThat(parser.findRoomCount("2-ушка"), is(2));
        assertThat(parser.findRoomCount("2-к бла квартира"), is(2));
        assertThat(parser.findRoomCount("2к бла кв"), is(2));
    }

    @Test
    public void parseThreeRoom() {
        assertThat(parser.findRoomCount("отличная трехкомнатная квартира"), is(3));
        assertThat(parser.findRoomCount("отличная трекомнатная квартира"), is(3));
        assertThat(parser.findRoomCount("отличную трехкомнатную квартиру"), is(3));
        assertThat(parser.findRoomCount("отличную трёхкомнатную квартиру"), is(3));
        assertThat(parser.findRoomCount("отличную трикомнатную квартиру"), is(3));
        assertThat(parser.findRoomCount("трешка"), is(3));
        assertThat(parser.findRoomCount("трёшка"), is(3));
        assertThat(parser.findRoomCount("трехкомнатная"), is(3));
        assertThat(parser.findRoomCount("трёхкомнатная"), is(3));
        assertThat(parser.findRoomCount("трех комнатная"), is(3));
        assertThat(parser.findRoomCount("три комнатная"), is(3));
        assertThat(parser.findRoomCount("трех комнатная"), is(3));
        assertThat(parser.findRoomCount("трёх комнатная"), is(3));
        assertThat(parser.findRoomCount("три комнаты"), is(3));
        assertThat(parser.findRoomCount("трех-комнатная"), is(3));
        assertThat(parser.findRoomCount("трёх-комнатная"), is(3));
        assertThat(parser.findRoomCount("три-комнатная"), is(3));
        assertThat(parser.findRoomCount("3-комнатная"), is(3));
        assertThat(parser.findRoomCount("3-ушка"), is(3));
        assertThat(parser.findRoomCount("3-ешка"), is(3));
        assertThat(parser.findRoomCount("3-ёшка"), is(3));
        assertThat(parser.findRoomCount("3ёшка"), is(3));
        assertThat(parser.findRoomCount("3-шка"), is(3));
        assertThat(parser.findRoomCount("3шка"), is(3));
        assertThat(parser.findRoomCount("3-к бла квартира"), is(3));
        assertThat(parser.findRoomCount("3к бла кв"), is(3));
    }
}