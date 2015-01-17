package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.dao.vk.VkItemRepository;
import bynull.realty.data.business.vk.Item;
import org.junit.Test;

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
public class VkItemRepositoryTest extends DbTest {
    @Resource
    VkItemRepository vkItemRepository;

    @Test
    public void insert() {

        Item item1 = new Item();
        item1.setDate(new Date());
        item1.setFromId("sdfsdfdsf");
        vkItemRepository.save(item1);

        flushAndClear();


        Item found = vkItemRepository.getOne(item1.getId());
        assertThat(found, is(notNullValue()));

    }

    @Test
    public void insertList() {
        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setDate(new Date());
        item1.setFromId("sdfsdf111");


        Item item2 = new Item();
        item2.setDate(new Date());
        item2.setFromId("sdfs2222");
        vkItemRepository.save(item2);

        Item item3 = new Item();
        item3.setDate(new Date());
        item3.setFromId("sdfsdf3333");
        vkItemRepository.save(item3);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        vkItemRepository.save(items);
        flushAndClear();

        List<Item> found = vkItemRepository.findAll();
        assertThat(found, is(notNullValue()));
    }
}
