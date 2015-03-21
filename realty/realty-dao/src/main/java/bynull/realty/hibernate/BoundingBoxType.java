package bynull.realty.hibernate;

import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.jdbc.PostgisDialect;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.postgis.PGbox2d;
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
public class BoundingBoxType implements UserType {
    @Override
    public int[] sqlTypes() {
        return new int[]{PostgisDialect.PGOBECT_TYPE};
    }

    @Override
    public Class returnedClass() {
        return BoundingBox.class;
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
        PGbox2d lastLocation = (PGbox2d) rs.getObject(names[0]);
        if (lastLocation != null) {
            BoundingBox bbox = new BoundingBox();

            Point lower = lastLocation.getLLB().getFirstPoint();
            Point upper = lastLocation.getURT().getFirstPoint();

            bbox.setLow(GeoPoint.from(lower));
            bbox.setHigh(GeoPoint.from(upper));

            return bbox;
        } else {
            return null;
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            //        st.setNull(index, new PGgeometry().getGeoType());
            st.setNull(index, Types.NULL);
        } else {
            BoundingBox bbox = (BoundingBox) value;
            PGbox2d box2d = new PGbox2d(bbox.getLow().toPoint(),bbox.getHigh().toPoint());
            st.setObject(index, box2d);
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
            BoundingBox geoPoint = new BoundingBox();
            geoPoint.setLow(((BoundingBox) original).getLow().copy());
            geoPoint.setHigh(((BoundingBox) original).getHigh().copy());
            return geoPoint;
        }

        return null;
    }
}
