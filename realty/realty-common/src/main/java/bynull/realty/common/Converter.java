package bynull.realty.common;

import java.util.Collection;
import java.util.List;

/**
 * Created by dionis on 18/01/15.
 */
public interface Converter<TT,
        ST> {
    List<TT> toTargetList(Collection<? extends ST> in);

    List<ST> toSourceList(Collection<? extends TT> in);

    TT toTargetType(ST in);

    ST toSourceType(TT in);
}
