package bynull.realty.grabber.json;

import java.util.List;

/**
 * Created by trierra on 12/6/14.
 */
public class WallPostJSON {

    private Long count;


    private List<ItemJSON> items;

    public WallPostJSON() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<ItemJSON> getItems() {
        return items;
    }

    public void setItems(List<ItemJSON> items) {
        this.items = items;
    }
}
