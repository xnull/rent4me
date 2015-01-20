package bynull.realty.components.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by dionis on 19/01/15.
 */
@Component
@Slf4j
@Lazy
public class MetroTextAnalyzer implements TextAnalyzer, InitializingBean {

    private Porter porter;
    private SynonymRegistry synonymRegistry;
    private Map<String, Pattern> patternCache = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
        synonymRegistry = RussianRegexStemSynonymRegistry.getInstance();
    }

    String regexPart(Set<String> synonyms) {
        String result = synonyms.stream().map(txt ->
                        "(\\b" + txt + (txt.endsWith("\\b") ? "" : "((\\S){0,3})") + ")"
        ).collect(Collectors.joining("|"));
        return "(" + result + ")";
    }

    private synchronized Pattern getPatternFromCacheOrSaveNew(String metroName) {
        String metroNameLowerCased = metroName.toLowerCase();

        if (!patternCache.containsKey(metroNameLowerCased)) {

            List<String> words = Arrays.asList(StringUtils.split(metroNameLowerCased));

            String collected = words.stream()
                    .map(porter::stem)
                    .map(synonymRegistry::getSynonyms)
                    .map(this::regexPart)
                            //расстояние между сджойненными словами от 1 до 5 произвольных символов
                    .collect(Collectors.joining(".{1,5}"));

            //найти по паттернам метряшки, затем нормализованные имена метряшек
            String regex = "((.*)((Ⓜ)|(\\bм\\b)|(\\bм\\.\\b)|(\\bметр\\S{1,3}\\b))((.){1,20})(" + collected + ")(.*))";
            System.out.println(regex);
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE);
            patternCache.put(metroNameLowerCased, pattern);
            return pattern;
        } else {
            return patternCache.get(metroNameLowerCased);
        }
    }

    @Override
    public boolean matches(String text, String metroName) {
        if (StringUtils.trimToEmpty(text).isEmpty() || StringUtils.trimToEmpty(metroName).isEmpty()) return false;

        text = StringUtils.replace(text, ".", ". ");
        text = StringUtils.replace(text, ",", ", ");
        text = StringUtils.replace(text, "\n", " ");
        text = StringUtils.replace(text, "\r", " ");
        text = StringUtils.replace(text, "  ", " ");

        return getPatternFromCacheOrSaveNew(metroName).matcher(text).matches();
    }
}
