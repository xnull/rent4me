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
public class PhoneNumberParser {
    public static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE;
    private static final PhoneNumberParser INSTANCE = new PhoneNumberParser();

    private final Pattern hasPhoneNumberPattern;

    private PhoneNumberParser() {
        hasPhoneNumberPattern = Pattern.compile("(.*)(\\bтел(\\D){0,5}(\\b|$))(.*)");
    }

    public static PhoneNumberParser getInstance() {
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

    public boolean hasPhoneNumber(String text) {
        log.info(">> Checking phone number process started");
        try {
            text = normalizeText(text);

            return hasPhoneNumberPattern.matcher(text).matches();
        } finally {
            log.info("<< Checking phone number process ended");
        }
    }

    @VisibleForTesting
    static class PatternCheck {
        public final Pattern pattern;
        public final int resultGroup;
        public final int multiplier;
        public final int[] resultGroupsThatShouldBeNull;

        public PatternCheck(Pattern pattern, int resultGroup, int multiplier, int[] resultGroupsThatShouldBeNull) {
            this.pattern = pattern;
            this.resultGroup = resultGroup;
            this.multiplier = multiplier;
            this.resultGroupsThatShouldBeNull = resultGroupsThatShouldBeNull;
        }

        public PatternCheck(Pattern pattern, int resultGroup, int multiplier) {
            this(pattern, resultGroup, multiplier, new int[0]);
        }

        public PatternCheck(Pattern pattern, int resultGroup) {
            this(pattern, resultGroup, 1);
        }
    }
}
