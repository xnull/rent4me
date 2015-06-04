package bynull.realty.services.api;

import bynull.realty.data.business.configs.ServerSetting;
import bynull.realty.dto.ServerSettingDTO;

import java.util.Optional;

/**
 * Created by dionis on 04/06/15.
 */
public interface ServerSettingsService {
    void save(ServerSettingDTO serverSettingDTO);
    Optional<ServerSettingDTO> getById(ServerSetting.Id id);
}
