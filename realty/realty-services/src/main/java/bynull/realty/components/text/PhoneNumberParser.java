package bynull.realty.components.text;

import bynull.realty.common.PhoneUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
