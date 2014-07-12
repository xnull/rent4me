package bynull.realty.util;

import java.util.Date;

/**
 * @author dionis on 12/07/14.
 */
public final class CommonUtils {
    private CommonUtils() {
    }

    /**
     * Perform null-safe copying of date.
     *
     * @param date date to copy.
     * @return new instance of date or null;
     */
    public static Date copy(Date date) {
        return date != null ? new Date(date.getTime()) : null;
    }
}
