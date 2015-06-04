package bynull.realty.dto;

import bynull.realty.data.business.configs.ServerSetting;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 04/06/15.
 */
@Getter
@Setter
public class ServerSettingDTO {
    private ServerSetting.Id id;
    private String value;

    public static ServerSettingDTO from(ServerSetting serverSetting) {
        if (serverSetting == null) {
            return null;
        }
        ServerSettingDTO dto = new ServerSettingDTO();
        dto.setId(serverSetting.getId());
        dto.setValue(serverSetting.getValue());
        return dto;
    }

    public static ServerSetting to(ServerSettingDTO dto) {
        if (dto == null) {
            return null;
        }
        ServerSetting serverSetting = new ServerSetting();
        serverSetting.setId(dto.getId());
        serverSetting.setValue(dto.getValue());
        return serverSetting;
    }


}
