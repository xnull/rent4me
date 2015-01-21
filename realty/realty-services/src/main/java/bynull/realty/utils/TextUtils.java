package bynull.realty.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dionis on 21/01/15.
 */
public class TextUtils {
    public static String normalizeTextForParsing(String text) {
        text = StringUtils.trimToEmpty(text);
        if (text.isEmpty()) return null;

        text = text.toLowerCase();
        text = StringUtils.replace(text, ".", ". ");
        text = StringUtils.replace(text, ",", ", ");
        text = StringUtils.replace(text, "'", "");
        text = StringUtils.replace(text, "\"", "");
        text = StringUtils.replace(text, "ё", "е");
        text = StringUtils.replace(text, "й", "и");
        text = StringUtils.replace(text, "\n", " ");
        text = StringUtils.replace(text, "\r", " ");
        text = StringUtils.replace(text, "  ", " ");

        return text;
    }

    public static String normalizeTextAggressivelyForParsing(String text) {
        text = normalizeTextForParsing(text);

        if (text == null) {
            return null;
        }

        text = StringUtils.replace(text, ".", "");
        text = StringUtils.replace(text, ",", "");
        text = StringUtils.replace(text, "!", "");
        text = StringUtils.replace(text, "?", "");

        text = normalizeTextForParsing(text);

        if (text == null) {
            return null;
        }

        return text;
    }
}
