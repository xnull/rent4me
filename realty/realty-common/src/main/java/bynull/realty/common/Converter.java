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
    default List<? extends TT> toTargetList(Collection<? extends ST> in) {
        if(in==null) return null;
        return in.stream().filter(it -> it != null).map(this::toTargetType).collect(Collectors.toList());
    }

    default List<? extends ST> toSourceList(Collection<? extends TT> in) {
        if(in==null) return null;
        return in.stream().filter(it -> it != null).map(this::toSourceType).collect(Collectors.toList());
    }

    default Set<? extends TT> toTargetSet(Collection<? extends ST> in) {
        if(in==null) return null;
        return in.stream().filter(it -> it != null).map(this::toTargetType).collect(Collectors.toSet());
    }

    default Set<? extends ST> toSourceSet(Collection<? extends TT> in) {
        if(in==null) return null;
        return in.stream().filter(it -> it != null).map(this::toSourceType).collect(Collectors.toSet());
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
