package bynull.realty.dao;

import bynull.realty.data.business.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by null on 7/27/15.
 */
public interface IdentificationRepository extends JpaRepository<Identification, Long> {
}
