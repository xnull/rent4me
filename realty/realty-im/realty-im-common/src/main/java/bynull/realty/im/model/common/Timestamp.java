package bynull.realty.im.model.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Getter
@ToString
@EqualsAndHashCode
public class Timestamp implements Comparable<Timestamp>, Serializable {
    private final Long timestamp;

    public Timestamp(@NonNull Long timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public int compareTo(Timestamp other) {
        return timestamp.compareTo(other.getTimestamp());
    }
}
