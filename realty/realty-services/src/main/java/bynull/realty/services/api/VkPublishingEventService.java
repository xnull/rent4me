package bynull.realty.services.api;

import bynull.realty.data.business.Apartment;

import java.util.Date;

/**
 * @author dionis
 *         08/06/15.
 */
public interface VkPublishingEventService {
    void publishEvent(Apartment.DataSource dataSource, String group, String text, String token);
    long countOfPublishedEventsWithToken(String token, Date since);

    long countOfPublishedEventsWithDataSource(Apartment.DataSource dataSource, Date tokenRestrictionPeriod);
}
