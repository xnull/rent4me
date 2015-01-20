package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import name.dargiri.web.form.MetroForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroAdminConverter implements Converter<MetroDTO, MetroForm> {

    @Override
    public MetroForm toTargetType(MetroDTO in) {
        if (in == null) return null;
        MetroForm form = new MetroForm();
        form.setId(in.getId());
        form.setStationName(in.getStationName());
        return form;
    }

    @Override
    public MetroDTO toSourceType(MetroForm in) {
        if (in == null) return null;
        MetroDTO dto = new MetroDTO();
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        return dto;
    }
}
