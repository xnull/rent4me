package bynull.realty.common;

import bynull.realty.common.Porter;
import org.junit.Before;
import org.junit.Test;

public class PorterTest {
    Porter porter;

    @Before
    public void setUp() throws Exception {
        porter = Porter.getInstance();
    }

    @Test
    public void learningWithPorter() {
//        assertThat(porter.stem("Киев"), is("киев"));
//        assertThat(porter.stem("Мая"), is("мая"));
    }
}