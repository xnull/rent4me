package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.Apartment;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;

public class ApartmentRepositoryTest extends DbTest {
    @Resource
    ApartmentRepository repository;

    @Test
    public void insert() {
        Apartment apartment = new Apartment();
        apartment = repository.saveAndFlush(apartment);

    }
}