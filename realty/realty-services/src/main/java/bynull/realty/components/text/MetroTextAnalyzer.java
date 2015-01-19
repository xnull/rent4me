package bynull.realty.components.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by dionis on 19/01/15.
 */
@Component
@Slf4j
@Lazy
public class MetroTextAnalyzer implements TextAnalyzer, InitializingBean {

    private Porter porter;
    private SynonymRegistry synonymRegistry;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
        synonymRegistry = RussianStemSynonymRegistry.getInstance();
    }

    @Override
    public boolean matches(String text, String metroName) {
        if (StringUtils.trimToEmpty(text).isEmpty() || StringUtils.trimToEmpty(metroName).isEmpty()) return false;
        String lowerCasedText = text.toLowerCase();

        String metroNameLowerCased = "метро " + metroName.toLowerCase();

        if (lowerCasedText.contains(metroNameLowerCased)) {
            log.info("100% match for metro [{}]", metroName);
            return true;
        } else {
            log.info("Trying to use stamming [{}]", metroName);
            //попытаться заюзать стеммер портера.
            // 1. разбиваем название метрячки по словам.
            LinkedList<String> words = new LinkedList<>(Arrays.asList(StringUtils.split(metroNameLowerCased)));
            int wordsCount = words.size();
            if (wordsCount == 0) {
                return false;
            }
            int succMatchCount = 0;


            Integer previousIndex = null;
            String previousMatchedStem = null;

            int currIndex = -1;

            List<String> candidateSubStrings = new ArrayList<>();
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                List<String> wordsLeft = words.subList(i + 1, wordsCount);
                if (previousIndex != null && previousIndex < 0) {
                    log.info("Previous index not found. Skipping");
                    break;
                }
                String stem = porter.stem(word);

                // 2. пытаемся найти на основе стемма(а так же его синонимов) для каждого слова входящие слова
                // так что бы они находились рядом друг с другом(на расстоянии скажем не более чем одно-два слово)
                //TODO: для каждого заматченного стемма генерить подстроки(кейс когда несколько вхождений подстроки могут присутствовать)
                Set<String> stemSynonyms = synonymRegistry.getSynonyms(stem);

                for (String stemSynonym : stemSynonyms) {
                    int idx = lowerCasedText.indexOf(stemSynonym);
                    if (idx != -1) {
                        if (previousIndex == null || idx > previousIndex) {
                            int startIdx = (previousIndex != null ? previousIndex : 0) + (previousMatchedStem != null ? previousMatchedStem.length() : 0);
                            int endIdx = idx + 1;
                            String wordsBetweenMatching = lowerCasedText.substring(startIdx, endIdx);
                            if (previousIndex == null || StringUtils.split(wordsBetweenMatching).length <= 2) {
                                //it's ok case - there's less or equal distance be
                                succMatchCount++;
                                previousMatchedStem = stemSynonym;
                                currIndex = idx;
                                break;
                            }
                        }
                    }
                }

                previousIndex = currIndex;

            }

            return succMatchCount == wordsCount;
        }
    }
}
