package bynull.realty.dao.api.ident;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by null on 8/3/15.
 */
public interface JpaRepositoryRent<T, ID extends Serializable> extends JpaRepository<T, ID> {

    default Optional<T> findOneOpt(ID id){
        return Optional.ofNullable(findOne(id));
    }
}
