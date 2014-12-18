package bynull.realty.crawler.json;

import java.util.Date;

/**
 * Created by trierra on 12/6/14.
 */
public class WallPost {
    private Long id;
    private Long fromId;
    private Date date;
    private String postType;
    private String text;


    public WallPost() {
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
