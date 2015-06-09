package bynull.realty.services.impl;

import bynull.realty.dao.vk.stats.VkPublishingEventRepository;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.vk.stats.VkPublishingEvent;
import bynull.realty.services.api.VkPublishingEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author dionis
 *         08/06/15.
 */
@Service
public class VkPublishingEventServiceImpl implements VkPublishingEventService {

    @Resource
    VkPublishingEventRepository vkPublishingEventRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void publishEvent(Apartment.DataSource dataSource, String group, String text, String token) {
        VkPublishingEvent event = new VkPublishingEvent();
        event.setDataSource(dataSource);
        event.setTextPublished(text);
        event.setTargetGroup(group);
        event.setUsedToken(token);
        vkPublishingEventRepository.saveAndFlush(event);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public long countOfPublishedEventsWithToken(String token, Date since) {
        return vkPublishingEventRepository.countOfPublishedEventWithTokenSince(token, since);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @Override
    public long countOfPublishedEventsWithDataSource(Apartment.DataSource dataSource, Date tokenRestrictionPeriod) {
        return vkPublishingEventRepository.countOfPublishedEventsWithDataSource(dataSource, tokenRestrictionPeriod);
    }
}
