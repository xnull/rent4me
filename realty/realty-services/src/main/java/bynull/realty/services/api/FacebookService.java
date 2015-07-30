package bynull.realty.services.api;

import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 02/01/15.
 */
public interface FacebookService {
    void syncWithFB();

    void save(FacebookPageDTO pageDTO);

    void delete(long id);

    List<FacebookPageDTO> listAllPages();

    Optional<FacebookPageDTO> findPageById(long fbPageId);

    List<FacebookPostDTO> findPosts(String text, PageRequest pageRequest);

    long countOfPages();

    void reparseExistingFBPosts();

    long countByQuery(String text);

}
