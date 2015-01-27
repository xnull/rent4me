package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.annotations.VisibleForTesting;
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
    @VisibleForTesting
    final PatternCheck fullPriceAbove_1000;
    @VisibleForTesting
    final PatternCheck fullPriceBellow_1000;
    private final List<PatternCheck> patterns;

    private RentalFeeParser() {
        String fullPriceBellow_1000_patternTemplate = "(\\b([\\d]{3}))";
        String fullPriceAbove_1000_patternTemplate = "(\\b([\\d]{1,3}(\\D)?[\\d]{3}))";

        //пример: цена 40 000
        PatternCheck simple = new PatternCheck(Pattern.compile(
                "(.*)((стоимост|бюджет|цен)([^\\s0-9]{0,2}))((\\D){1,5})(\\b(([\\d]{0,3})(\\s)?[\\d]{3}))((\\s){0,2})(р((\\S{1,6})|(\\.)))?(.*)", FLAGS), 7);

        //пример: 40 000 цена
        PatternCheck simpleInverseAbove1000 = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceAbove_1000_patternTemplate + "((\\s){0,2})(р((\\S{1,6})|(\\.)))?((\\D){0,5})((стоимост|бюджет|цен)((\\D){1,5})([^\\s0-9]{0,2}))(.*)", FLAGS), 2);

        //пример: 400 цена
        PatternCheck simpleInverseBellow1000 = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceBellow_1000_patternTemplate + "((\\s){0,2})(р((\\S{1,6})|(\\.)))?((\\D){0,5})((стоимост|бюджет|цен)((\\D){1,5})([^\\s0-9]{0,2}))(.*)", FLAGS), 2);

        //пример: цена 40 000 - 45 000
        PatternCheck rangeBothComplete = new PatternCheck(Pattern.compile(
                "(.*)((стоимост|бюджет|цен)([^\\s0-9]{0,2}))((\\D){1,5})(\\b(([\\d]{0,3})(\\s)?[\\d]{3}))((\\D){1,5})(\\b(([\\d]{0,3})(\\s)?[\\d]{3}))((\\s){0,2})(р((\\S{1,6})|(\\.)))?(.*)", FLAGS), 13);

        //пример: цена 40 - 45 000
        PatternCheck rangeStartInComplete = new PatternCheck(Pattern.compile(
                "(.*)((стоимост|бюджет|цен)([^\\s0-9]{0,2}))((\\D){1,5})(\\b([\\d]{1,3}))((\\D){1,5})(\\b(([\\d]{0,3})(\\s)?[\\d]{3}))((\\s){0,2})(р((\\S{1,6})|(\\.)))?(.*)", FLAGS), 11);

        //пример: 45 000
        fullPriceAbove_1000 = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceAbove_1000_patternTemplate + "([\\D](.*)|$)", FLAGS), 2);
        //пример: 45 000
        fullPriceBellow_1000 = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceBellow_1000_patternTemplate + "([\\D](.*)|$)", FLAGS), 2);

        PatternCheck patternCheckForThousandsWithWords = new PatternCheck(
                Pattern.compile(
                        "(.*)((\\D){1,5})(\\b([\\d]{1,3}))(([\\D]){0,3})(тыс)(.*)", FLAGS),
                4,
                1000
        );

        PatternCheck patternCheckForThousandsNonStrictAbbreviation = new PatternCheck(
                Pattern.compile(
                        "(.*)((\\D){1,5})(\\b([\\d]{1,3}))(([\\D]){0,3})(тыр)(.*)", FLAGS),
                4,
                1000
        );

        PatternCheck patternCheckForThousandsAggressiveAbbreviation = new PatternCheck(
                Pattern.compile(
                        "(.*)((\\D){1,5})(\\b([\\d]{1,3}))(([\\D]){0,3})(т([\\s]{0,2})р)(.*)", FLAGS),
                4,
                1000
        );

        patterns = ImmutableList.of(
                simpleInverseAbove1000,
                simpleInverseBellow1000,
                simple,
                rangeBothComplete,
                rangeStartInComplete,
                fullPriceAbove_1000,
                fullPriceBellow_1000,
                patternCheckForThousandsWithWords,
                patternCheckForThousandsNonStrictAbbreviation,
                patternCheckForThousandsAggressiveAbbreviation
        );
    }

    public static RentalFeeParser getInstance() {
        return INSTANCE;
    }

    public BigDecimal findRentalFee(String text) {
        text = StringUtils.trimToEmpty(text).toLowerCase();
        if (text.isEmpty()) return null;

        text = TextUtils.normalizeTextAggressivelyForParsing(text);

        for (PatternCheck patternCheck : patterns) {
            Pattern pattern = patternCheck.pattern;
            Matcher matcher = pattern.matcher(text);
            if (matcher.matches()) {
                log.info("Price matched by pattern [{}]", matcher.pattern());
                String value = StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup));
                value = StringUtils.replace(value, " ", "");
                try {
                    BigDecimal bigDecimal = new BigDecimal(value);
                    if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0) {
                        return bigDecimal.multiply(BigDecimal.valueOf(patternCheck.multiplier));
                    } else {
                        log.error("Parsing error. Value parsed: [{}]", bigDecimal);
                    }
                } catch (Exception e) {
                    log.error("Exception occurred while parsing result value [" + value + "]", e);
                }
            }
        }

        return null;
    }

    @VisibleForTesting
    static class PatternCheck {
        public final Pattern pattern;
        public final int resultGroup;
        public final int multiplier;

        public PatternCheck(Pattern pattern, int resultGroup, int multiplier) {
            this.pattern = pattern;
            this.resultGroup = resultGroup;
            this.multiplier = multiplier;
        }

        public PatternCheck(Pattern pattern, int resultGroup) {
            this(pattern, resultGroup, 1);
        }
    }
}
