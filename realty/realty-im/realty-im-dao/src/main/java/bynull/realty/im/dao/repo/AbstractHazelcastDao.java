package bynull.realty.im.dao.repo;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */

import bynull.realty.im.model.KeysUtil;
import bynull.realty.im.model.KeysUtil.ImKey;
import bynull.realty.im.model.KeysUtil.ImMapKey;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Общий дао для всех hazelcast репозиториев
 *
 * @param <T>
 */
@Repository
public abstract class AbstractHazelcastDao<T> {
    @Autowired
    protected HazelcastInstance repo;
    @Autowired
    protected KeysUtil keysUtil;

    protected <K, V> IMap<K, V> getMap(ImMapKey<K, V> key) {
        return repo.getMap(key.getKey());
    }

    protected IList<T> getList(ImKey<T> key) {
        return repo.getList(key.getKey());
    }
}
