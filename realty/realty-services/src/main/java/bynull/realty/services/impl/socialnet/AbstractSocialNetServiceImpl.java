package bynull.realty.services.impl.socialnet;

import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.dao.MetroRepository;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.MetroDTO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dionis on 3/11/15.
 */
@Slf4j
public class AbstractSocialNetServiceImpl {

    @Resource
    MetroTextAnalyzer metroTextAnalyzer;

    @Resource
    MetroRepository metroRepository;

    protected Set<MetroEntity> matchMetros(List<? extends MetroDTO> metros, String message) {
        log.info(">> Matching metros started");
        try {
            Set<MetroEntity> matchedMetros = new HashSet<>();
            for (MetroDTO metro : metros) {
                if (metroTextAnalyzer.matches(message, metro.getStationName())) {
                    //                log.info("Post #matched to metro #[] ({})", metro.getId(), metro.getStationName());

//                    matchedMetros.add(metroRepository.findOne(metro.getId()));
                    MetroEntity e = new MetroEntity();
                    e.setId(metro.getId());
                    matchedMetros.add(e);

                }
            }
            return matchedMetros;
        } finally {
            log.info("<< Matching metros ended");
        }
    }
}