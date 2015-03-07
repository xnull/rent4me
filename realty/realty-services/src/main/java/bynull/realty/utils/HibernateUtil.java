package bynull.realty.utils;

import org.hibernate.proxy.HibernateProxy;

/**
 * Created by dionis on 3/5/15.
 */
public class HibernateUtil {
    public static <T> T deproxy(Object o) {
        if(o instanceof HibernateProxy) {
            return (T) ((HibernateProxy)o).getHibernateLazyInitializer().getImplementation();
        }
        return (T) o;
    }
}
