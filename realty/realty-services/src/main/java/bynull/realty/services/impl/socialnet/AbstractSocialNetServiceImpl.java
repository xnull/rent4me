package bynull.realty.services.impl.socialnet;

import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.dao.MetroRepository;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.MetroDTO;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/11/15.
 */
@Slf4j
public class AbstractSocialNetServiceImpl {

    @Resource
    MetroTextAnalyzer metroTextAnalyzer;

    @Resource
    MetroRepository metroRepository;

    protected Set<MetroEntity> matchMetros(List<? extends MetroDTO> metros, String message, Optional<CityDTO> optCity) {
        log.info(">> Matching metros started");

        try {
            Set<MetroEntity> matchedMetros = new HashSet<>();
            metros = optCity.isPresent() ?
                    metros.stream()
                            .filter(m -> m.getCity().getId().equals(optCity.get().getId()))
                            .collect(Collectors.toList())
                    : metros;
            for (MetroDTO metro : metros) {
                if (metroTextAnalyzer.matches(message, metro.getStationName())) {
                    //                log.info("Post #matched to metro #[] ({})", metro.getId(), metro.getStationName());

                    matchedMetros.add(metroRepository.findOne(metro.getId()));
//                    MetroEntity e = new MetroEntity();
//                    e.setId(metro.getId());
//                    matchedMetros.add(e);

                }
            }
            return matchedMetros;
        } finally {
            log.info("<< Matching metros ended");
        }
    }

    protected GeoPoint getAveragePoint(Set<MetroEntity> metroEntities) {
        if (metroEntities == null || metroEntities.isEmpty()) return null;

        int counter = metroEntities.size();
        double latSum = 0.0d;
        double lngSum = 0.0d;

        for (MetroEntity metroEntity : metroEntities) {
            lngSum += metroEntity.getLocation().getLongitude();
            latSum += metroEntity.getLocation().getLatitude();
        }

        double averageLat = latSum / counter;
        double averageLng = lngSum / counter;

        return new GeoPoint().withLatitude(averageLat).withLongitude(averageLng);
    }

    protected GeoPoint getAveragePoint(Optional<CityDTO> cityDTO) {
        if (!cityDTO.isPresent()) return null;

        double averageLat = (cityDTO.get().getArea().getHigh().getLatitude() + cityDTO.get().getArea().getLow().getLatitude()) / 2;
        double averageLng = (cityDTO.get().getArea().getHigh().getLongitude() + cityDTO.get().getArea().getLow().getLongitude()) / 2;

        return new GeoPoint().withLatitude(averageLat).withLongitude(averageLng);
    }
}
