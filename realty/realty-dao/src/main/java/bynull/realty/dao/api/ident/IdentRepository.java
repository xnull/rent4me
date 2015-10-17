package bynull.realty.dao.api.ident;

import bynull.realty.dao.util.IdentRefiner;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Created by null on 7/27/15.
 */
public interface IdentRepository extends JpaRepository<IdentEntity, Long> {

    @Repository
    @Slf4j
    class IdentRepositorySimple {
        @PersistenceContext
        EntityManager em;

        public Optional<IdentEntity> find(String ident, String type) {
            log.trace("Find an ident: {}", ident, type);

            String qlString = "select ident from IdentEntity ident where (ident.identValue like :v and ident.identType = :t)";
            TypedQuery<IdentEntity> query = em.createQuery(qlString, IdentEntity.class);
            query.setParameter("v", IdentRefiner.refine(ident, IdentType.from(type)));
            query.setParameter("t", type);

            List<IdentEntity> resultList = query.getResultList();

            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
        }
    }
}
