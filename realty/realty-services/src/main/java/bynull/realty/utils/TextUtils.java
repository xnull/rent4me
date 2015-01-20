package bynull.realty.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dionis on 21/01/15.
 */
public class TextUtils {
    public static String normalizeTextForParsing(String text) {
        if (text == null) return null;

        text = StringUtils.replace(text, ".", ". ");
        text = StringUtils.replace(text, ",", ", ");
        text = StringUtils.replace(text, "ё", "е");
        text = StringUtils.replace(text, "й", "и");
        text = StringUtils.replace(text, "\n", " ");
        text = StringUtils.replace(text, "\r", " ");
        text = StringUtils.replace(text, "  ", " ");

        return text;
    }
}
