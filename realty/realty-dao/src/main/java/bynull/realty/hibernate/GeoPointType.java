package bynull.realty.hibernate;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.jdbc.GeoPointColumnMapper;
import bynull.realty.jdbc.PostgisDialect;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgis.PGgeometry;
import org.postgis.Point;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author dionis on 22/06/14.
 */
public class GeoPointType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{PostgisDialect.GEOMETRY_TYPE};
    }

    @Override
    public Class returnedClass() {
        return GeoPoint.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || x != null && y != null && x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        return GeoPointColumnMapper.INSTANCE.mapGeoPoint(rs, names[0]);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            //        st.setNull(index, new PGgeometry().getGeoType());
            st.setNull(index, Types.NULL);
        } else {
            Point point = new Point();
            GeoPoint geoPoint = (GeoPoint) value;
            point.setX(geoPoint.getLongitude());
            point.setY(geoPoint.getLatitude());
            point.setSrid(4326);
            st.setObject(index, new PGgeometry(point));
        }

    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        if (original != null) {
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLongitude(((GeoPoint) original).getLongitude());
            geoPoint.setLatitude(((GeoPoint) original).getLatitude());
            return geoPoint;
        }

        return null;
    }
}
