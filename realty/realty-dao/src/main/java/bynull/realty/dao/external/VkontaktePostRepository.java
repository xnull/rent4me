package bynull.realty.dao.external;

import bynull.realty.data.business.external.vkontakte.VkontaktePost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dionis on 03/02/15.
 */
@Deprecated
public interface VkontaktePostRepository extends JpaRepository<VkontaktePost, Long> {
    List<VkontaktePost> findByExternalIdIn(List<String> externalIds);

    @Query("select p from VkontaktePost p where lower(p.message) like :text order by created desc")
    List<VkontaktePost> findByQuery(@Param("text") String text, Pageable pageable);

    @Query("select count(p) from VkontaktePost p where lower(p.message) like :text")
    long countByQuery(@Param("text") String text);

    @Query("select p from VkontaktePost p where p.externalId=:externalId order by created desc")
    List<VkontaktePost> findByExternalIdNewest(@Param("externalId") String externalId, Pageable pageable);
}
