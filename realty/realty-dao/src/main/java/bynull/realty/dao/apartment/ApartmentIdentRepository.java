package bynull.realty.dao.apartment;

import bynull.realty.data.business.apartment.ApartmentIdent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by null on 8/17/15.
 */
public interface ApartmentIdentRepository extends JpaRepository<ApartmentIdent, Long> {

    ApartmentIdent findByApartmentIdAndIdentId(Long apartmentId, Long identId);
}
