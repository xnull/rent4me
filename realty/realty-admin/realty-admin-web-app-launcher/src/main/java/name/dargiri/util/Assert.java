package name.dargiri.util;

import java.net.URL;

/**
 * Created by dionis on 2/2/14.
 */
public class Assert {
    public static void notNull(Object o) {
        if(o == null) throw new IllegalArgumentException("Null value not allowed");
    }
}
