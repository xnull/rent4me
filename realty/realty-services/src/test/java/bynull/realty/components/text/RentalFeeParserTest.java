package bynull.realty.components.text;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
    }
}