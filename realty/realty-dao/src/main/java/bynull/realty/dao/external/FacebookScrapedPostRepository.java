package bynull.realty.dao.external;

import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dionis on 02/01/15.
 */
public interface FacebookScrapedPostRepository extends JpaRepository<FacebookScrapedPost, Long> {
    List<FacebookScrapedPost> findByExternalIdIn(List<String> externalIds);
    @Query("select p from FacebookScrapedPost p where p.externalId=:externalId order by created desc")
    List<FacebookScrapedPost> findByExternalIdNewest(@Param("externalId")String externalId, Pageable pageable);
}
