package bynull.realty.components.text;

import bynull.realty.common.PhoneUtil;
import bynull.realty.utils.TextUtils;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dionis on 21/01/15.
 */
@Slf4j
public class PhoneNumberParser {
    private static final PhoneNumberParser INSTANCE = new PhoneNumberParser();

    private PhoneNumberParser() {
    }

    public static PhoneNumberParser getInstance() {
        return INSTANCE;
    }

    public boolean hasPhoneNumber(String text) {
        log.info(">> Checking phone number process started");
        try {
            return !findPhoneNumbers(text).isEmpty();
        } finally {
            log.info("<< Checking phone number process ended");
        }
    }

    public List<PhoneUtil.Phone> findPhoneNumbers(String text) {
        return PhoneUtil.findPhoneNumbers(text, "RU");
    }
}
