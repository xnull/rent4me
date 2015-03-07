package bynull.realty.components.text;

import bynull.realty.common.Porter;
import org.springframework.util.Assert;

/**
 * Created by dionis on 19/01/15.
 */
public class RussianRegexStemSynonymRegistry extends SynonymRegistry {

    private static final RussianRegexStemSynonymRegistry INSTANCE = new RussianRegexStemSynonymRegistry();
    private final Porter porter = Porter.getInstance();

    private RussianRegexStemSynonymRegistry() {
        String ulicaStem = porter.stem("улица");
        Assert.isTrue(ulicaStem.equals("улиц"));

        String metroStem = porter.stem("метро");
        Assert.isTrue(metroStem.equals("метр"));

        String bulvarStem = porter.stem("бульвар");
        Assert.isTrue(bulvarStem.equals("бульвар"));

        registerSynonyms("ул\\.", ulicaStem);
        registerSynonyms("у\\.", ulicaStem);
        registerSynonyms("у\\b", ulicaStem);
//        registerSynonyms(ulicaStem, ulicaStem+"\\S*");
        registerSynonyms("м\\.", metroStem);
        registerSynonyms("Ⓜ", metroStem);
        registerSynonyms("м\\b", metroStem);

        registerSynonyms("бульв\\.", bulvarStem);
        registerSynonyms("бульв\\b", bulvarStem);
        registerSynonyms("блв\\.", bulvarStem);
        registerSynonyms("блв\\b", bulvarStem);
        registerSynonyms("б\\.", bulvarStem);
        registerSynonyms("б\\b", bulvarStem);

        freeze();
    }

    public static RussianRegexStemSynonymRegistry getInstance() {
        return INSTANCE;
    }
}
