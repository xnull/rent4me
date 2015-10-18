package bynull.realty.dao;

import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;

/**
 * Created by null on 10/18/15.
 */
public class JooqUtil {

    public static DefaultDSLContext getContext(){
        return new DefaultDSLContext(SQLDialect.POSTGRES_9_3);
    }
}
