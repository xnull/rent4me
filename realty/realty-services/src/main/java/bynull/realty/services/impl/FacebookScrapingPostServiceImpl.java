package bynull.realty.services.impl;

import bynull.realty.components.FacebookHelperComponent;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.dao.external.FacebookScrapedPostRepository;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.services.api.FacebookScrapingPostService;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 02/01/15.
 */
@Service
public class FacebookScrapingPostServiceImpl implements FacebookScrapingPostService {
    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;

    @Resource
    FacebookScrapedPostRepository facebookScrapedPostRepository;

    @Resource
    FacebookHelperComponent facebookHelperComponent;

    @PersistenceContext
    EntityManager em;

    @Transactional
    @Override
    public void scrapNewPosts() {
        List<FacebookPageToScrap> fbPages = facebookPageToScrapRepository.findAll();
        em.clear();//detach all instances
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (FacebookPageToScrap fbPage : fbPages) {
            List<FacebookScrapedPost> newest = facebookScrapedPostRepository.findByExternalIdNewest(fbPage.getExternalId(), getLimit1Offset0());

            Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getCreated();

            List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOs = facebookHelperComponent.loadPostsFromPage(fbPage.getExternalId(), maxPostsAgeToGrab);
            List<FacebookScrapedPost> byExternalIdIn = !facebookPostItemDTOs.isEmpty() ? facebookScrapedPostRepository.findByExternalIdIn(facebookPostItemDTOs.stream()
                            .map(FacebookHelperComponent.FacebookPostItemDTO::getId)
                            .collect(Collectors.toList())
            ) : Collections.emptyList();
            Set<String> ids = byExternalIdIn.stream()
                    .map(FacebookScrapedPost::getExternalId)
                    .collect(Collectors.toSet());

            List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOsToPersist = facebookPostItemDTOs
                    .stream()
                    .filter(i -> !ids.contains(i.getId()))
                    .collect(Collectors.toList());

            em.clear();

            Iterable<List<FacebookHelperComponent.FacebookPostItemDTO>> partitions = Iterables.partition(facebookPostItemDTOsToPersist, 20);
            for (List<FacebookHelperComponent.FacebookPostItemDTO> partition : partitions) {
                FacebookPageToScrap page = new FacebookPageToScrap(fbPage.getId());
                for (FacebookHelperComponent.FacebookPostItemDTO postItemDTO : partition) {
                    FacebookScrapedPost post = postItemDTO.toInternal();
                    post.setFacebookPageToScrap(page);
                    facebookScrapedPostRepository.save(post);
                }
                em.flush();
                em.clear();
            }
            em.flush();
        }
    }

    private PageRequest getLimit1Offset0() {
        return new PageRequest(0, 1);
    }
}
