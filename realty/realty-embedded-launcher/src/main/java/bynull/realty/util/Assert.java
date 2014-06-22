package bynull.realty.util;

/**
 * @author dionis on 22/06/14.
 */
public class Assert {
    public static void notNull(Object o) {
        if(o == null) throw new IllegalArgumentException("Null value not allowed");
    }
}
