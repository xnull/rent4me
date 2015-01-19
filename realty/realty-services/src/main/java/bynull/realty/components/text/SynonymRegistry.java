package bynull.realty.components.text;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by dionis on 19/01/15.
 */
@Slf4j
public abstract class SynonymRegistry {
    private final Map<String, Set<String>> map = new HashMap<>();
    private volatile boolean frozen = false;

    protected final void freeze() {
        frozen = true;
    }

    public void registerSynonyms(String a, String b) {
        if (frozen) {
            throw new IllegalStateException("Can not register synonyms any more when state is frozem");
        }
        Assert.notNull(a);
        Assert.notNull(b);
        /**
         * Map share the same instance of a set for all synonyms
         */
        Collection<Set<String>> values = map.values();
        Set<String> targetSet = null;
        for (Set<String> value : values) {
            if (value.contains(a) || value.contains(b)) {
                targetSet = value;
                break;
            }
        }
        //nothing had a set
        if (targetSet == null) {
            targetSet = Sets.newHashSetWithExpectedSize(2);
        }
        targetSet.add(a);
        targetSet.add(b);
        if (!map.containsKey(a)) {
            map.put(a, targetSet);
        }
        if (!map.containsKey(b)) {
            map.put(b, targetSet);
        }
    }

    public Set<String> getSynonyms(String s) {
        return Collections.unmodifiableSet(map.getOrDefault(s, Collections.emptySet()));
    }
}
