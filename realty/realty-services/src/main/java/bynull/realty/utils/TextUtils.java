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
        text = StringUtils.replace(text, "?", "? ");
        text = StringUtils.replace(text, "!", "! ");
        text = StringUtils.replace(text, "'", "");
        text = StringUtils.replace(text, "\"", "");
        text = StringUtils.replace(text, "ё", "е");
        text = StringUtils.replace(text, "й", "и");
        text = StringUtils.replace(text, "\n", " ");
        text = StringUtils.replace(text, "\r", " ");
        text = StringUtils.replace(text, "<br", " <br");
        text = replaceRecursively(text, "  ", " ");
        //if percent was used with number then this could break it
        text = replaceRecursively(text, " %", "%");

        return text;
    }

    public static String normalizeTextForUrlParsing(String text) {
        text = StringUtils.trimToEmpty(text);
        if (text.isEmpty()) return null;

        text = text.toLowerCase();

        text = StringUtils.replace(text, ",", ", ");
        text = StringUtils.replace(text, "!", "! ");
        text = StringUtils.replace(text, "\"", "\" ");
        text = StringUtils.replace(text, "'", "' ");

        text = StringUtils.replace(text, "\n", " ");
        text = StringUtils.replace(text, "\r", " ");
        text = replaceRecursively(text, "  ", " ");

        return text;
    }

    public static String replaceRecursively(String text, String search, String replace) {
        String prev;
        while (true) {
            prev = text;
            text = StringUtils.replace(text, search, replace);
            if (text.equals(prev))
                return text;
        }
    }

    public static String normalizeTextAggressivelyForPriceParsing(String text) {
        String initialText = text;
        text = normalizeTextForParsing(text);

        if (text == null) {
            return null;
        }

        text = StringUtils.replace(text, ".", "");
        text = StringUtils.replace(text, ",", "");
        text = StringUtils.replace(text, "!", "");
        text = StringUtils.replace(text, "?", "");
        text = StringUtils.replace(text, ":", "");
        text = text.replaceAll("([0-9]+)", " $1 ");

        text = normalizeTextForParsing(text);

        if (text == null) {
            return null;
        }

        if (initialText.equals(text)) return text;
        else return normalizeTextAggressivelyForPriceParsing(text);
    }
}
