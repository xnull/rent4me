package bynull.realty.dao;

import bynull.realty.data.business.metro.MetroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author null
 * @since 1/9/15.
 */
public interface MetroRepository extends JpaRepository<MetroEntity, Long>, MetroRepositoryCustom {

}
