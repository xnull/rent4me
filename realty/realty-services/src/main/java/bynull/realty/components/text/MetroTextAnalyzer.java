package bynull.realty.components.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

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
        synonymRegistry = RussianSynonymRegistry.getInstance();
    }

    @Override
    public boolean matches(String text, String metroName) {
        if (StringUtils.trimToEmpty(text).isEmpty() || StringUtils.trimToEmpty(metroName).isEmpty()) return false;
        String lowerCasedText = text.toLowerCase();
        String metroNameLowerCased = metroName.toLowerCase();

        if (lowerCasedText.contains(metroNameLowerCased)) {
            log.info("100% match for metro [{}]", metroName);
            return true;
        } else {
            log.info("Trying to use stamming [{}]", metroName);
            //попытаться заюзать стеммер портера.
            // 1. разбиваем название метрячки по словам.
            String[] split = StringUtils.split(metroNameLowerCased);
            int len = split.length;
            if (len == 0) {
                return false;
            }
            int succMatchCount = 0;
            for (String s : split) {
                String stem = porter.stem(s);
                // 2. TODO: пытаемся найти на основе стемма для каждого слова входящие слова так что бы они находились рядом друг с другом(на расстоянии скажем не более ем одно-два слово)
                if (lowerCasedText.contains(stem)) {
                    succMatchCount++;
                } else {
                    log.info("Exact match by stem not found, attempt to find by synonyms of stem [{}]", stem);
                    Set<String> stemSynonyms = synonymRegistry.getSynonyms(stem);
                    for (String stemSynonym : stemSynonyms) {
                        if (lowerCasedText.contains(stemSynonym)) {
                            log.info("Matched by stem synonym [{}]", stemSynonym);
                            succMatchCount++;
                            break;
                        }
                    }
                }
            }

            return succMatchCount == len;
        }
    }
}
