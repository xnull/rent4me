package bynull.realty.dao;

import bynull.realty.data.business.PhotoTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
public interface PhotoTempRepository extends JpaRepository<PhotoTemp, Long> {
    @Modifying
    @Query("delete from PhotoTemp t where t.created <= :date")
    void garbageCollect(@Param("date") Date date);

    List<PhotoTemp> findByGuidIn(List<String> guids);

    @Query("select t from PhotoTemp t where t.created <= :date")
    List<PhotoTemp> findPlacesForGarbageCollection(@Param("date") Date date);
}
