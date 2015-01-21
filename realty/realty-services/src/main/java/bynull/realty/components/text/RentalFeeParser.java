package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dionis on 21/01/15.
 */
@Slf4j
public class RentalFeeParser {
    public static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE;
    private static final RentalFeeParser INSTANCE = new RentalFeeParser();
    private final List<PatternCheck> patterns;

    private RentalFeeParser() {
        patterns = ImmutableList.of(
//                Pattern.compile("(.*)(цена)((\\D){1,5})(\\b(([\\d]{3,8})|(([\\d]{0,3})(\\s)?[\\d]{3}))\\b)((\\s){0,2})р((\\S{1,6})|(\\.))?(.*)")
                new PatternCheck(Pattern.compile("(.*)(цена)((\\D){1,5})(\\b(([\\d]{0,3})(\\s)?[\\d]{3}))((\\s){0,2})р((\\S{1,6})|(\\.))?(.*)", FLAGS), 5)
        );
    }

    public static RentalFeeParser getInstance() {
        return INSTANCE;
    }

    public BigDecimal findRentalFee(String text) {
        text = StringUtils.trimToEmpty(text).toLowerCase();
        if (text.isEmpty()) return null;

        text = TextUtils.normalizeTextForParsing(text);

        for (PatternCheck patternCheck : patterns) {
            Pattern pattern = patternCheck.pattern;
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                String value = StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup));
                value = StringUtils.replace(value, " ", "");
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    return bigDecimal;
                } catch (Exception e) {
                    log.error("Exception occurred while parsing result value [" + value + "]", e);
                }
            }
        }

        return null;
    }

    private static class PatternCheck {
        public final Pattern pattern;
        public final int resultGroup;

        public PatternCheck(Pattern pattern, int resultGroup) {
            this.pattern = pattern;
            this.resultGroup = resultGroup;
        }
    }
}
