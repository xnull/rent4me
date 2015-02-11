package bynull.realty.components.text;

import bynull.realty.utils.TextUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
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
    private final PhoneNumberParser phoneNumberParser;

    private RentalFeeParser() {
        final String fullPriceBellow_1000_patternTemplate = "(\\b([\\d]{3}))";
        final String fullPriceAbove_1000_patternTemplate = "(\\b([\\d]{1,3}(\\D)?[\\d]{3}))";
        phoneNumberParser = PhoneNumberParser.getInstance();

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

        //пример: 45 000 руб, 45 000 рублей
        PatternCheck fullPriceAbove_1000WithCurrency = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceAbove_1000_patternTemplate + "([\\D]{0,2}руб([\\D]{0,4})(.*)|$)", FLAGS), 2);
        //пример: 450 руб, 450 рублей
        PatternCheck fullPriceBellow_1000WithCurrency = new PatternCheck(Pattern.compile(
                "(.*)" + fullPriceBellow_1000_patternTemplate + "([\\D]{0,2}руб([\\D]{0,4})(.*)|$)", FLAGS), 2);

        //пример: 45 000
        List<PatternCheck.SuccessCallbackChecker> successCallbackCheckers = Collections.singletonList(new PhoneNumberSuccessCallbackChecker());
        fullPriceAbove_1000 = new PatternCheck(Pattern.compile(

                "(.*)" +
                        fullPriceAbove_1000_patternTemplate +
                        "([\\D](.*)|$)", FLAGS), 2, 1, successCallbackCheckers);

        //пример: 45 000
        fullPriceBellow_1000 = new PatternCheck(Pattern.compile(

                "(.*)" +
                        fullPriceBellow_1000_patternTemplate +
                        "([\\D](.*)|$)", FLAGS), 2, 1, successCallbackCheckers);

        patterns = ImmutableList.of(
                simpleInverseAbove1000,
                simpleInverseBellow1000,
                simple,
                rangeBothComplete,
                rangeStartInComplete,
                patternCheckForThousandsWithWords,
                patternCheckForThousandsNonStrictAbbreviation,
                patternCheckForThousandsAggressiveAbbreviation,
                fullPriceAbove_1000WithCurrency,
                fullPriceBellow_1000WithCurrency,
                fullPriceAbove_1000,
                fullPriceBellow_1000
        );
    }

    public static RentalFeeParser getInstance() {
        return INSTANCE;
    }

    @VisibleForTesting
    String normalizeText(String text) {
        String result;
        result = StringUtils.trimToEmpty(text).toLowerCase();
        if (result.isEmpty()) return null;

        result = TextUtils.normalizeTextAggressivelyForParsing(text);
        return result;
    }

    public BigDecimal findRentalFee(String text) {
        log.info(">> Finding rental fee process started");
        try {
            text = normalizeText(text);

            pattern_loop: for (PatternCheck patternCheck : patterns) {
                Pattern pattern = patternCheck.pattern;
                Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    log.debug("Price matched by pattern [{}]", matcher.pattern());
                    String value = StringUtils.trimToEmpty(matcher.group(patternCheck.resultGroup));
                    value = StringUtils.replace(value, " ", "");
                    try {
                        BigDecimal bigDecimal = new BigDecimal(value);
                        if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0) {
                            List<PatternCheck.SuccessCallbackChecker> successCheckCallbacks = patternCheck.successCheckCallbacks;
                            for (PatternCheck.SuccessCallbackChecker callback : successCheckCallbacks) {
                                if(!callback.checkOnSuccess(text)) {
                                    log.debug("Specified result was invalidated by callback [{}]. Skipping", callback);
                                    continue pattern_loop;
                                }
                            }
                            return bigDecimal.multiply(BigDecimal.valueOf(patternCheck.multiplier));
                        } else {
                            log.error("Parsing error. Value parsed: [{}]", bigDecimal);
                        }
                    } catch (Exception ignore) {
    //                    log.error("Exception occurred while parsing result value [{}]: {}", value, e.getMessage());
                    }
                }
            }

            return null;
        } finally {
            log.info("<< Finding rental fee process ended");
        }
    }

    @VisibleForTesting
    static class PatternCheck {
        public final Pattern pattern;
        public final int resultGroup;
        public final int multiplier;
        public List<SuccessCallbackChecker> successCheckCallbacks;

        public PatternCheck(Pattern pattern, int resultGroup, int multiplier, List<SuccessCallbackChecker> callbacks) {
            this.pattern = pattern;
            this.resultGroup = resultGroup;
            this.multiplier = multiplier;
            this.successCheckCallbacks = ImmutableList.copyOf(callbacks);
        }

        public PatternCheck(Pattern pattern, int resultGroup, int multiplier) {
            this(pattern, resultGroup, multiplier, Collections.emptyList());
        }

        public PatternCheck(Pattern pattern, int resultGroup) {
            this(pattern, resultGroup, 1);
        }

        static interface SuccessCallbackChecker {
            boolean checkOnSuccess(String text);
        }
    }

    private class PhoneNumberSuccessCallbackChecker implements PatternCheck.SuccessCallbackChecker {

        @Override
        public boolean checkOnSuccess(String text) {
            return !phoneNumberParser.hasPhoneNumber(text);
        }
    }
}
