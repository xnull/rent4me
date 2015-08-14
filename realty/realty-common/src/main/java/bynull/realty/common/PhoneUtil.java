package bynull.realty.common;


import com.google.common.collect.Iterables;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        public Optional<String> getPhoneNumber(){
            return national != null ? Optional.of(national) : Optional.ofNullable(raw);
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
            String raw = phoneNumber.rawString();
            if(raw == null || raw.isEmpty()) continue;
            result.add(new Phone(raw, national));
        }

        return result;
    }

    public static Phone findPhoneSafeRu(String text) {
        return findFirstPhoneNumberSafe(text, "RU");
    }

    public static Phone findFirstPhoneNumberSafe(String text, String countryCode) {
        return Iterables.getFirst(findPhoneNumbers(text, countryCode), new Phone(text, null));
    }

    public static Phone findFirstPhoneNumber(String text, String countryCode) {
        return Iterables.getFirst(findPhoneNumbers(text, countryCode), null);
    }
}
