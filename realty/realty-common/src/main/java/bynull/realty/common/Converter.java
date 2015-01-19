package bynull.realty.common;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 18/01/15.
 */
public interface Converter<TT,
        ST> {
    default List<TT> toTargetList(Collection<? extends ST> in) {
        return in.stream().map(this::toTargetType).collect(Collectors.toList());
    }

    default List<ST> toSourceList(Collection<? extends TT> in) {
        return in.stream().map(this::toSourceType).collect(Collectors.toList());
    }

    TT toTargetType(ST in);

    ST toSourceType(TT in);
}
