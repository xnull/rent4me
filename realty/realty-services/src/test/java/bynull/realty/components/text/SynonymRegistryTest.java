package bynull.realty.components.text;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SynonymRegistryTest {

    SynonymRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new SynonymRegistry() {

        };
    }

    @Test
    public void registerSynonyms() {
        registry.registerSynonyms("улица", "ул.");
        registry.registerSynonyms("улица", "ул");
        registry.registerSynonyms("у.", "ул.");

        Set<String> synonyms = registry.getSynonyms("у.");
        assertThat(synonyms.size(), is(4));
        assertThat(synonyms.contains("улица"), is(true));
        assertThat(synonyms.contains("ул."), is(true));
        assertThat(synonyms.contains("ул"), is(true));
        assertThat(synonyms.contains("у."), is(true));
    }
}