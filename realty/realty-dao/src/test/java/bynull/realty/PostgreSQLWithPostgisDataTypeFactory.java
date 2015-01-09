package bynull.realty;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 22/06/14.
 */
@Component
public class PostgreSQLWithPostgisDataTypeFactory extends PostgresqlDataTypeFactory {
    @Resource
    private GeometryDataType geometryDataType;

    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if ("geometry".equals(sqlTypeName)) {
            return geometryDataType;
        } else {
            return super.createDataType(sqlType, sqlTypeName);
        }
    }
}
