package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.dao.vk.VkRepository;
import bynull.realty.data.business.vk.Item;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by trierra on 1/13/15.
 */
public class VkRepositoryTest extends DbTest {
    @Resource
    VkRepository vkRepository;

    @Test
    @Rollback(false)
    public void insert() {

        Item item1 = new Item();
        item1.setDate(new Date());
        item1.setFromId("sdfsdfdsf");
        vkRepository.save(item1);

        flushAndClear();


        Item found = vkRepository.getOne(item1.getId());
        assertThat(found, is(notNullValue()));

    }

    @Test
    @Rollback(false)
    public void insertList() {
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setDate(new Date());
        item1.setFromId("sdfsdf111");


        Item item2 = new Item();
        item2.setDate(new Date());
        item2.setFromId("sdfs2222");
        vkRepository.save(item2);

        Item item3 = new Item();
        item3.setDate(new Date());
        item3.setFromId("sdfsdf3333");
        vkRepository.save(item3);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        vkRepository.save(items);
        flushAndClear();

        List<Item> found = vkRepository.findAll();
        assertThat(found, is(notNullValue()));
    }
}
