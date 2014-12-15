package bynull.realty.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;

/**
 * @author dionis on 15/12/14.
 */
public class LimitAndOffset {
    public final int limit;
    public final int offset;

    private LimitAndOffset(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public <T extends PageRequest> T toPageable(Class<T> targetClass) {
        try {
            Constructor<T> constructor = targetClass.getConstructor(int.class, int.class);
            return constructor.newInstance(offset/limit, limit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int limit;
        private int offset;

        public Builder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public LimitAndOffset create() {
            Assert.isTrue(offset >= 0, "Offset should be greater or equal to zero");
            Assert.isTrue(limit >= 0, "Limit should be greater or equal to zero");
            return new LimitAndOffset(limit, offset);
        }
    }

    @Override
    public String toString() {
        return "LimitAndOffset{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
