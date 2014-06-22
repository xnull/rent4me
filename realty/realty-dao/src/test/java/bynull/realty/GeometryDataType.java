package bynull.realty;

import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * @author dionis on 22/06/14.
 */
@Component
public class GeometryDataType extends AbstractDataType {
    @Resource
    private JdbcOperations jdbcOperations;

    public GeometryDataType() {
        super("geometry", Types.OTHER, String.class, false);
    }

    public Object getSqlValue(int column, ResultSet resultSet) throws SQLException, TypeCastException {
        String content = resultSet.getString(column);
        if (content == null) {
            return null;
        } else {
            return jdbcOperations.queryForObject("select St_AsText('" + content + "')", String.class);
        }
    }

    public void setSqlValue(Object geom, int column, PreparedStatement statement) throws SQLException, TypeCastException {
        statement.setObject(column, getGeometry(geom, statement.getConnection()));
    }

    public Object typeCast(Object arg0) throws TypeCastException {

        return arg0.toString();

    }

    private Object getGeometry(Object value, Connection connection) throws TypeCastException {
        Object tempgeom = null;

        try {
            Class aPGIntervalClass = super.loadClass("org.postgis.PGgeometry", connection);

            Constructor ct = aPGIntervalClass.getConstructor(new Class[]{String.class});

            tempgeom = ct.newInstance(new Object[]{value});

        } catch (ClassNotFoundException e) {

            throw new TypeCastException(value, this, e);

        } catch (InvocationTargetException e) {

            throw new TypeCastException(value, this, e);

        } catch (NoSuchMethodException e) {

            throw new TypeCastException(value, this, e);

        } catch (IllegalAccessException e) {

            throw new TypeCastException(value, this, e);

        } catch (InstantiationException e) {

            throw new TypeCastException(value, this, e);

        }

        return tempgeom;

    }
}
