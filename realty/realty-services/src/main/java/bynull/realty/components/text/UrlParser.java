package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dionis on 2/15/15.
 */
public class UrlParser {
    private static final UrlParser INSTANCE = new UrlParser();

    public static UrlParser getInstance() {
        return INSTANCE;
    }

    private final List<Pattern> patterns;

    private UrlParser() {

        String pattern = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]\\b";
        patterns = ImmutableList.of(Pattern.compile(pattern));
    }

    public Set<String> findUrls(String text) {
        String normalizedText = TextUtils.normalizeTextForUrlParsing(text);

        Set<String> urls = new HashSet<>();

        for (Pattern pattern : patterns) {
            Matcher m = pattern.matcher(normalizedText);
            while(m.find()) {
                String url = m.group();
                String trimmedUrl = StringUtils.trimToEmpty(url);
                if(!trimmedUrl.isEmpty()) {
                    urls.add(trimmedUrl);
                }
            }
        }

        return urls;
    }
}
