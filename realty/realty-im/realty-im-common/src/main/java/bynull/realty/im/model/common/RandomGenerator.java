package bynull.realty.im.model.common;

import org.apache.commons.lang.RandomStringUtils;

import java.security.SecureRandom;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 14.12.14.
 */
public class RandomGenerator {
    private static final SecureRandom RND = new SecureRandom();

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length).toUpperCase();
    }
}
