package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
}
