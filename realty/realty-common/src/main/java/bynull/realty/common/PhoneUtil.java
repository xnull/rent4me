package bynull.realty.common;


import com.google.common.collect.Iterables;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dionis on 2/11/15.
 */
public class PhoneUtil {

    public static class Phone {
        public final String raw;
        public final String national;

        public Phone(String raw, String national) {
            this.raw = raw;
            this.national = national;
        }

        @Override
        public String toString() {
            return "Phone{" +
                    "raw='" + raw + '\'' +
                    ", national='" + national + '\'' +
                    '}';
        }
    }

    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    public static List<Phone> findPhoneNumbers(String text, String countryCode) {
        if (text == null) {
            return Collections.emptyList();
        }

        Iterable<PhoneNumberMatch> phoneNumbers = PHONE_NUMBER_UTIL.findNumbers(text, countryCode, PhoneNumberUtil.Leniency.POSSIBLE, Long.MAX_VALUE);
        List<Phone> result = new ArrayList<>();
        for (PhoneNumberMatch phoneNumber : phoneNumbers) {
            Phonenumber.PhoneNumber number = phoneNumber.number();
            String national = PHONE_NUMBER_UTIL.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            result.add(new Phone(phoneNumber.rawString(), national));
        }

        return result;
    }

    public static Phone findFirstPhoneNumber(String text, String countryCode) {
        return Iterables.getFirst(findPhoneNumbers(text, countryCode), null);
    }
}
