/**
 * @author dionis on 22/06/14.
 */
@TypeDefs({
        @TypeDef(defaultForType = GeoPoint.class, typeClass = GeoPointType.class)
}) package bynull.realty.dao;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.hibernate.GeoPointType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;