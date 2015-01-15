package bynull.realty.services.impl.socialnet.fb;

import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.FacebookScrapedPostRepository;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.data.business.metro.MetroEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Парсинг московских групп по аренде жилья
 *
 * @author Vyacheslav Petc
 * @since 1/16/15.
 */
@Component
@Slf4j
public class FbParserForMoscow {
    @Autowired
    private MetroRepository metroRepository;
    @Autowired
    private FacebookScrapedPostRepository fbPostsRepository;

    public void parse() {
        log.debug("Parse fb posts");

        List<MetroEntity> metroStations = metroRepository.findAll();

        for (FacebookScrapedPost fbPost : fbPostsRepository.findAll()) {
            for (MetroEntity metroStation : metroStations) {
                if (fbPost.getMessage().contains(metroStation.getStationName())) {
                    //Нашли станцию в объявлении, надо теперь присобачить метро с объявлению
                    continue;
                }
            }
        }
    }
}
