package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import name.dargiri.web.form.GeoPointForm;
import name.dargiri.web.form.MetroForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroAdminConverter implements Converter<MetroDTO, MetroForm> {

    @Override
    public MetroForm newTargetType(MetroDTO in) {
        return new MetroForm();
    }

    @Override
    public MetroDTO newSourceType(MetroForm in) {
        return new MetroDTO();
    }

    @Override
    public MetroForm toTargetType(MetroDTO in, MetroForm form) {
        if (in == null) return null;
        form.setId(in.getId());
        form.setStationName(in.getStationName());
        form.setLocation(GeoPointForm.from(in.getLocation()));
        return form;
    }

    @Override
    public MetroDTO toSourceType(MetroForm in, MetroDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        dto.setLocation(GeoPointForm.toDTO(in.getLocation()));
        return dto;
    }
}
