package bynull.realty.dao.vk.stats;

import bynull.realty.DbTest;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.vk.stats.VkPublishingEvent;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class VkPublishingEventRepositoryTest extends DbTest {
    @Resource
    VkPublishingEventRepository repository;

    @Test
    public void persistTest() {
        VkPublishingEvent event = new VkPublishingEvent();
        event.setTargetGroup("grp1");
        event.setTextPublished("txt1");
        event.setUsedToken("token1");
        event.setDataSource(Apartment.DataSource.VKONTAKTE);
        event = repository.saveAndFlush(event);
        flushAndClear();

        assertThat(event.getId(), is(notNullValue()));
    }
}