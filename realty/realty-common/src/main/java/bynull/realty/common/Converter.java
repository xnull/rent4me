package bynull.realty.common;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 18/01/15.
 */
public interface Converter<ST,
        TT> {
    default List<TT> toTargetList(Collection<? extends ST> in) {
        return in.stream().map(this::toTargetType).collect(Collectors.toList());
    }

    default List<ST> toSourceList(Collection<? extends TT> in) {
        return in.stream().map(this::toSourceType).collect(Collectors.toList());
    }

    default Set<TT> toTargetSet(Collection<? extends ST> in) {
        return in.stream().map(this::toTargetType).collect(Collectors.toSet());
    }

    default Set<ST> toSourceSet(Collection<? extends TT> in) {
        return in.stream().map(this::toSourceType).collect(Collectors.toSet());
    }

    TT newTargetType(ST in);
    ST newSourceType(TT in);

    TT toTargetType(ST in, TT instance);

    default TT toTargetType(ST in) {
        return toTargetType(in, newTargetType(in));
    }

    ST toSourceType(TT in, ST instance);

    default ST toSourceType(TT in) {
        return toSourceType(in, newSourceType(in));
    }
}
