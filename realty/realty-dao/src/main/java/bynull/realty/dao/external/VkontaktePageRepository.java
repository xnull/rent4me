package bynull.realty.dao.external;

import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by dionis on 28/01/15.
 */
public interface VkontaktePageRepository extends JpaRepository<VkontaktePage, Long> {

    default Optional<VkontaktePage> findOneOpt(long vkPageId) {
        return Optional.ofNullable(findOne(vkPageId));
    }
}
