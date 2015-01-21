package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dionis on 21/01/15.
 */
public class RentalFeeParser {
    private static final RentalFeeParser INSTANCE = new RentalFeeParser();
    private final List<Pattern> patterns;

    private RentalFeeParser() {
        patterns = ImmutableList.of(
                Pattern.compile("(.*)(цена)((\\D){1,5})(\\b([\\d\\s]{3,8})\\b)р((\\S{1,6})|(\\.))?(.*)")
        );
    }

    public static RentalFeeParser getInstance() {
        return INSTANCE;
    }

    public BigDecimal findRentalFee(String text) {
        text = StringUtils.trimToEmpty(text).toLowerCase();
        if (text.isEmpty()) return null;

        text = TextUtils.normalizeTextForParsing(text);

        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                System.out.println("Matches!");
                String value = StringUtils.trimToEmpty(matcher.group(5));
                value = StringUtils.replace(value, " ", "");
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    return bigDecimal;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
