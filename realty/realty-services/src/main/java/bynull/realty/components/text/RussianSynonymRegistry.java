package bynull.realty.components.text;

/**
 * Created by dionis on 19/01/15.
 */
public class RussianSynonymRegistry extends SynonymRegistry {

    private static final RussianSynonymRegistry INSTANCE = new RussianSynonymRegistry();

    private RussianSynonymRegistry() {
        registerSynonyms("ул.", "улиц");
        registerSynonyms("у.", "улиц");
        registerSynonyms("у.", "улиц");

        registerSynonyms("бульв.", "бульвар");
        registerSynonyms("блв.", "бульвар");
        registerSynonyms("б.", "бульвар");

        freeze();
    }

    public static RussianSynonymRegistry getInstance() {
        return INSTANCE;
    }
}
