package bynull.realty.jdbc;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dionis on 22/06/14.
 */
public class PostgisDialect extends PostgreSQL9Dialect {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgisDialect.class);

    public static final int GEOMETRY_TYPE = -100500;
    public static final int GEOGRAPHY_TYPE = -100501;

    public PostgisDialect() {
        LOGGER.info(">>>>> registering hibernate types and functions to support postgis");
        registerColumnType(GEOMETRY_TYPE, "geometry");
        registerColumnType(GEOGRAPHY_TYPE, "geography");
        LOGGER.info("<<<<< registered hibernate types and functions to support postgis");
    }
}
