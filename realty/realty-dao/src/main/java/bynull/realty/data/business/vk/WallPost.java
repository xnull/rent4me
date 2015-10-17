package bynull.realty.data.business.vk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trierra on 12/6/14.
 */
public class WallPost {
    private Long count;
    private List<Item> items = new ArrayList<>();

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
