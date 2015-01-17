package bynull.realty.dao.vk;

import bynull.realty.data.business.vk.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by trierra on 12/24/14.
 */
public interface VkItemRepository extends JpaRepository<Item, Long> {

}
