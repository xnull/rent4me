package bynull.realty.common;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The converter
 * Created by dionis on 18/01/15.
 */
public interface Converter<ST, TT> {
    default List<? extends TT> toTargetList(Collection<? extends ST> in) {
        if (in == null) return Collections.emptyList();
        return in.stream()
                .filter(it -> it != null)
                .map(it -> toTargetType(Optional.of(it)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default List<? extends ST> toSourceList(Collection<? extends TT> in) {
        if (in == null) return Collections.emptyList();
        return in.stream().filter(it -> it != null).map(this::toSourceType).collect(Collectors.toList());
    }

    default Set<? extends TT> toTargetSet(Collection<? extends ST> in) {
        if (in == null) return Collections.emptySet();
        return in.stream()
                .filter(it -> it != null)
                .map(it -> toTargetType(Optional.of(it)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    default Set<? extends ST> toSourceSet(Collection<? extends TT> in) {
        if (in == null) return Collections.emptySet();
        return in.stream().filter(it -> it != null).map(this::toSourceType).collect(Collectors.toSet());
    }

    @Deprecated
    default TT newTargetType(ST in) {
        throw new UnsupportedOperationException("Deperesated method, please use 'newTargetType' with Optional ");
    }

    default TT newTargetType(Optional<ST> in){
        return newTargetType(in.orElse(null));
    }

    ST newSourceType(TT in);

    Optional<TT> toTargetType(Optional<ST> in, TT instance);

    @Deprecated
    default Optional<TT> toTargetType(ST in) {
        Optional<ST> optIn = Optional.ofNullable(in);
        return toTargetType(optIn, newTargetType(optIn));
    }

    default Optional<TT> toTargetType(Optional<ST> in) {
        return toTargetType(in, newTargetType(in));
    }

    ST toSourceType(TT in, ST instance);

    default ST toSourceType(TT in) {
        return toSourceType(in, newSourceType(in));
    }
}
