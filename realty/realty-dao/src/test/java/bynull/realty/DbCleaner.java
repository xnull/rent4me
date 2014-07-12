package bynull.realty;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 22/06/14.
 */
@Component
public class DbCleaner implements InitializingBean {
    @Resource
    private JdbcOperations jdbcOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        Iterable<String> tablesToDelete = jdbcOperations.queryForList(
                "SELECT table_name " +
                        "FROM " +
                        "information_schema.tables " +
                        "WHERE " +
                        "table_schema = 'public' " +
                        "and table_catalog='realty_testdb' " +
                        "and upper(table_type)<>'VIEW' " +
                        "and table_name not in ('spatial_ref_sys', 'geography_columns', 'geometry_columns', 'raster_columns', 'raster_overviews')"
                , String.class);
        Iterable<String> dropTableSQLs = Iterables.transform(tablesToDelete, new Function<String, String>() {
            @Override
            public String apply(String tableName) {
                return "DROP TABLE " + tableName + " CASCADE";
            }
        });

        for (String dropTableSQL : dropTableSQLs) {
            jdbcOperations.execute(dropTableSQL);
        }
    }
}
