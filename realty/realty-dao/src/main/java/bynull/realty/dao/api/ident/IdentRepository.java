package bynull.realty.dao.api.ident;

import bynull.realty.common.PhoneUtil;
import bynull.realty.dao.util.IdentRefiner;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Created by null on 7/27/15.
 */
public interface IdentRepository extends JpaRepository<IdentEntity, Long> {

    @Repository
    class IdentRepositorySimple {
        @PersistenceContext
        EntityManager em;

        public IdentEntity find(String ident, String type) {
            String qlString = "select ident from IdentEntity ident where (ident.identValue like :v and ident.identType = :t)";
            TypedQuery<IdentEntity> query = em.createQuery(qlString, IdentEntity.class);
            query.setParameter("v", IdentRefiner.refine(ident, IdentType.from(type)));
            query.setParameter("t", type);

            List<IdentEntity> resultList = query.getResultList();

            return resultList.isEmpty() ? null : resultList.get(0);
        }
    }
}
