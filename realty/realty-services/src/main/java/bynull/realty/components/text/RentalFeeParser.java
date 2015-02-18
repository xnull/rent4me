package bynull.realty.components.text;

import bynull.realty.common.PhoneUtil;
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
    private static final String HOT_WORD_STAMS_FOR_PRICE = "((цена вопроса)|(стоимост|бюджет|цен|оплат|аренда))";
    //руб/рублей/р./руб./
    private static final String RUBLES_PATTERN = "(([\\D]{0,2}\\brub([\\D]{0,4}))|([\\D]{0,2}\\bруб([\\D]{0,4}))|([\\D]{0,2}\\bр\\b([\\D]{0,5})))";

    @VisibleForTesting
    final PatternCheck fullPriceAbove_1000;
    @VisibleForTesting
    final PatternCheck fullPriceBellow_1000;
    private final List<PatternCheck> patterns;
    private final PhoneNumberParser phoneNumberParser;
    private final UrlParser urlParser;

    private RentalFeeParser() {
        List<PatternCheck.SuccessCallbackChecker> successCallbackCheckers = Collections.singletonList(new PhoneNumberSuccessCallbackChecker());

        final String fullPriceBellow_1000_patternTemplate = "((\\b([\\d]{3}))\\b)";
        final String fullPriceAbove_1000_patternTemplate = "(\\b(((([\\d]?)((\\D)?))([\\d]{1,3})(\\D)?)[\\d]{3,4})\\b)";
        phoneNumberParser = PhoneNumberParser.getInstance();
        urlParser = UrlParser.getInstance();

        //пример: цена 40 000
        PatternCheck simple = new PatternCheck(Pattern.compile(
                "(" + HOT_WORD_STAMS_FOR_PRICE + "([^\\s0-9]{0,2}))((\\D){1,5})"+fullPriceAbove_1000_patternTemplate+"((\\s){0,2})(р((\\S{1,6})|(\\.)))?", FLAGS), 9);

        //пример: 40 000 цена
        PatternCheck simpleInverseAbove1000 = new PatternCheck(Pattern.compile(
                fullPriceAbove_1000_patternTemplate + "((\\s){0,2})(р((\\S{1,6})|(\\.)))?((\\D){0,5})(" + HOT_WORD_STAMS_FOR_PRICE + "((\\D){1,5})([^\\s0-9]{0,2}))", FLAGS), 2);

        //пример: 400 цена
        PatternCheck simpleInverseBellow1000 = new PatternCheck(Pattern.compile(
                fullPriceBellow_1000_patternTemplate + "((\\s){0,2})(р((\\S{1,6})|(\\.)))?((\\D){0,5})(" + HOT_WORD_STAMS_FOR_PRICE + "((\\D){1,5})([^\\s0-9]{0,2}))", FLAGS), 2);

        //пример: цена 40 000 - 45 000
        PatternCheck rangeBothComplete = new PatternCheck(Pattern.compile(
                "(" + HOT_WORD_STAMS_FOR_PRICE + "([^\\s0-9]{0,2}))((\\D){1,5})"+fullPriceAbove_1000_patternTemplate+"((\\D){1,5})"+fullPriceAbove_1000_patternTemplate+"((\\s){0,2})(р((\\S{1,6})|(\\.)))?", FLAGS), 13);

        //пример: цена 40 - 45 000
        PatternCheck rangeStartInComplete = new PatternCheck(Pattern.compile(
                "(" + HOT_WORD_STAMS_FOR_PRICE + "([^\\s0-9]{0,2}))((\\D){1,5})(\\b([\\d]{1,3}))((\\D){1,5})"+"+fullPriceAbove_1000_patternTemplate+"+"((\\s){0,2})", FLAGS), 11);

        //TODO: support тысяч рублей, тысячей рублей, ттысяч р тысяч рублей
        PatternCheck patternCheckForThousandsWithWords = new PatternCheck(
                Pattern.compile(
                        "((\\D){1,5}|^)(\\b([\\d]{1,3}))(([\\D]){0,3})((\\bтыс)|(\\bтыр)|(\\bт\\b)|(\\bт([\\s]{0,2})(\\w)?р))", FLAGS),
                4,
                1000
        );

        //пример: 45 000 руб, 45 000 рублей
        PatternCheck fullPriceAbove_1000WithCurrency = new PatternCheck(Pattern.compile(
                fullPriceAbove_1000_patternTemplate + "("+RUBLES_PATTERN+")", FLAGS), 2);
        //пример: 450 руб, 450 рублей
        PatternCheck fullPriceBellow_1000WithCurrency = new PatternCheck(Pattern.compile(
                fullPriceBellow_1000_patternTemplate + "("+RUBLES_PATTERN+")", FLAGS), 2);

        //пример: 45 000
        fullPriceAbove_1000 = new PatternCheck(Pattern.compile(
                fullPriceAbove_1000_patternTemplate, FLAGS), 2, 1, successCallbackCheckers);

        //пример: 450
        fullPriceBellow_1000 = new PatternCheck(Pattern.compile(

                        fullPriceBellow_1000_patternTemplate, FLAGS), 2, 1, successCallbackCheckers);

        patterns = ImmutableList.of(
                patternCheckForThousandsWithWords,
                simple,
                simpleInverseAbove1000,
                simpleInverseBellow1000,
                rangeBothComplete,
                rangeStartInComplete,
                fullPriceAbove_1000WithCurrency,
                fullPriceBellow_1000WithCurrency,
                fullPriceAbove_1000
//                ,fullPriceBellow_1000
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

        result = TextUtils.normalizeTextAggressivelyForPriceParsing(text);
        return result;
    }

    public BigDecimal findRentalFee(String inputText) {
        log.info(">> Finding rental fee process started");
        try {
            String text = normalizeText(inputText);
            if(text == null) return null;

            pattern_loop: for (PatternCheck patternCheck : patterns) {
                Pattern pattern = patternCheck.pattern;
                Matcher matcher = pattern.matcher(text);
//                int matchedPos = 0;
                BigDecimal resultValue = null;

                while (matcher.find()) {
//                    matchedPos++;
                    log.debug("Price matched by pattern [{}]", matcher.pattern());
                    String value = normalizeMatchedValue(matcher.group(patternCheck.resultGroup));
                    try {
                        BigDecimal bigDecimal = new BigDecimal(value);
                        if (bigDecimal.compareTo(BigDecimal.ZERO) >= 0) {
                            List<PatternCheck.SuccessCallbackChecker> successCheckCallbacks = patternCheck.successCheckCallbacks;
                            for (PatternCheck.SuccessCallbackChecker callback : successCheckCallbacks) {
                                if(!callback.allowed(text, value)) {
                                    log.debug("Specified result was invalidated by callback [{}]. Skipping", callback);
                                    continue pattern_loop;
                                }
                            }

                            long longValBeforeMultiplying = bigDecimal.longValue();
                            if((longValBeforeMultiplying > 10_000 && longValBeforeMultiplying%100 != 0) ||
                                    (longValBeforeMultiplying > 100_000 && longValBeforeMultiplying%1_000 != 0)) {
                                log.warn("Seems that value is a little bit ugly ");
                                continue pattern_loop;
                            }

                            BigDecimal tmpResultValue = bigDecimal.multiply(BigDecimal.valueOf(patternCheck.multiplier));
                            if(tmpResultValue.longValue() > 500_000) {
                                log.warn("Value [{}] is too big. Skipping", tmpResultValue);
                                continue pattern_loop;
                            }

//                            if(tmpResultValue.equals(BigDecimal.ZERO)) {
//                                log.warn("Value [{}] is zero. Skipping FURTHER analysis. Some weird shit is with this value", tmpResultValue);
//                                continue pattern_loop;
//                            }

                            if(resultValue == null || tmpResultValue.compareTo(resultValue) > 0) {
                                log.info("Setting result value to [{}]", tmpResultValue);
                                resultValue = tmpResultValue;
                            }
                        } else {
                            log.error("Parsing error. Value parsed: [{}]", bigDecimal);
                        }
                    } catch (Exception ignore) {
    //                    log.error("Exception occurred while parsing result value [{}]: {}", value, e.getMessage());
                    }
                }

                if(resultValue != null) {
                    return !resultValue.equals(BigDecimal.ZERO) ? resultValue : null;
                }
            }

            return null;
        } finally {
            log.info("<< Finding rental fee process ended");
        }
    }

    private String normalizeMatchedValue(String group) {
        return TextUtils.replaceRecursively(StringUtils.trimToEmpty(group), " ", "");
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
            boolean allowed(String text, String foundValue);
        }
    }

    private class PhoneNumberSuccessCallbackChecker implements PatternCheck.SuccessCallbackChecker {

        @Override
        public boolean allowed(String text, String context) {
            boolean found = false;
            List<PhoneUtil.Phone> phoneNumbers = phoneNumberParser.findPhoneNumbers(text);
            for (PhoneUtil.Phone phoneNumber : phoneNumbers) {
                if(normalizeMatchedValue(phoneNumber.raw).contains(context)) {
                    found = true;
                    break;
                }
            }
            return !found;
        }
    }
}
