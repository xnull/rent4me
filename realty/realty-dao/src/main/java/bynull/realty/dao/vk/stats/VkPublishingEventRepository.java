package bynull.realty.dao.vk.stats;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.vk.stats.VkPublishingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.UUID;

/**
 * @author dionis
 *         07/06/15.
 */
public interface VkPublishingEventRepository extends JpaRepository<VkPublishingEvent, UUID> {
    @Query("select count(e) from VkPublishingEvent e where e.usedToken=:token and e.created >= :since")
    long countOfPublishedEventWithTokenSince(@Param("token")String token, @Param("since")Date since);

    @Query("select count(e) from VkPublishingEvent e where e.dataSource=:dataSource and e.created >= :since")
    long countOfPublishedEventsWithDataSource(@Param("dataSource")Apartment.DataSource dataSource, @Param("since")Date tokenRestrictionPeriod);
}
