package bynull.realty.components.text;

import org.springframework.util.Assert;

/**
 * Created by dionis on 19/01/15.
 */
public class RussianStemSynonymRegistry extends SynonymRegistry {

    private static final RussianStemSynonymRegistry INSTANCE = new RussianStemSynonymRegistry();
    private final Porter porter = Porter.getInstance();

    private RussianStemSynonymRegistry() {
        String ulicaStem = porter.stem("улица");
        Assert.isTrue(ulicaStem.equals("улиц"));

        String metroStem = porter.stem("метро");
        Assert.isTrue(metroStem.equals("метр"));

        String bulvarStem = porter.stem("бульвар");
        Assert.isTrue(bulvarStem.equals("бульвар"));

        registerSynonyms("ул.", ulicaStem);
        registerSynonyms("у.", ulicaStem);
        registerSynonyms("у ", ulicaStem);
        registerSynonyms("м.", metroStem);
        registerSynonyms("м ", metroStem);

        registerSynonyms("бульв.", bulvarStem);
        registerSynonyms("бульв ", bulvarStem);
        registerSynonyms("блв.", bulvarStem);
        registerSynonyms("блв ", bulvarStem);
        registerSynonyms("б.", bulvarStem);
        registerSynonyms("б ", bulvarStem);

        freeze();
    }

    public static RussianStemSynonymRegistry getInstance() {
        return INSTANCE;
    }
}
