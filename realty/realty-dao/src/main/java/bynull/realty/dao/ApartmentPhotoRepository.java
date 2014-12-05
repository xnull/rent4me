package bynull.realty.dao;

import bynull.realty.data.business.ApartmentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
public interface ApartmentPhotoRepository extends JpaRepository<ApartmentPhoto, Long> {
    List<ApartmentPhoto> findByGuidIn(List<String> guids);
}
