package bynull.realty.data.business.vk;

import java.util.List;

/**
 * Created by trierra on 12/6/14.
 */
public class WallPost extends BaseEntity {

    private Long count;


    private List<Item> items;

    public WallPost() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
