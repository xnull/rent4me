package bynull.realty.dao.apartment;

import bynull.realty.DbTest;
import bynull.realty.data.business.Apartment;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by null on 10/18/15.
 */
public class ApartmentRepositoryImplTest {

    @Test
    public void testSimilarApartmentsQuery() throws Exception {
        assertEquals(
                "",
                ApartmentRepositoryImpl.similarApartmentsQuery().replaceAll("\n", " ")
        );
    }
}