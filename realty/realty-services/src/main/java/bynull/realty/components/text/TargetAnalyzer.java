package bynull.realty.components.text;

import bynull.realty.data.business.Apartment;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dionis on 3/7/15.
 */
@Component
public class TargetAnalyzer {
    private static final List<String> LESSOR_TARGETED_KEYWORDS = ImmutableList.of("сниму", "снимаю", "снять", "снял", "возьму", "возьмет", "взял", "взять", "снимем", "снимет", "снимут");
    private static final List<String> RENTER_TARGETED_KEYWORDS = ImmutableList.of("сдаю", "сдам", "отдам", "отдаю", "отдается", "отдает",
            "сдается", "подселю", "сдаем", "сдает",
            //слова долбоеб стайл
            "здаем", "здадим", "здам"
    );

    public Apartment.Target determineTarget(String text) {
        String lowerCasedText = StringUtils.trimToEmpty(StringUtils.lowerCase(text));
        if(lowerCasedText.isEmpty()) return Apartment.Target.UNKNOWN;

        boolean hasLessorTargetedKeywords = false;

        for (String lessorTargetedKeyword : LESSOR_TARGETED_KEYWORDS) {
            hasLessorTargetedKeywords = lowerCasedText.contains(lessorTargetedKeyword);
            if(hasLessorTargetedKeywords)
                break;
        }

        boolean hasRenterTargetedKeywords = false;
        for (String renterTargetedKeyword : RENTER_TARGETED_KEYWORDS) {
            hasRenterTargetedKeywords = lowerCasedText.contains(renterTargetedKeyword);
            if(hasRenterTargetedKeywords) {
                break;
            }
        }

        if(hasLessorTargetedKeywords && hasRenterTargetedKeywords) {
            return Apartment.Target.BOTH;
        } else if(hasLessorTargetedKeywords) {
            return Apartment.Target.LESSOR;
        } else if(hasRenterTargetedKeywords) {
            return Apartment.Target.RENTER;
        } else {
            return Apartment.Target.UNKNOWN;
        }

    }
}
