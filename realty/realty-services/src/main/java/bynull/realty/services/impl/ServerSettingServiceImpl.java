package bynull.realty.services.impl;

import bynull.realty.dao.ServerSettingRepository;
import bynull.realty.data.business.configs.ServerSetting;
import bynull.realty.dto.ServerSettingDTO;
import bynull.realty.services.api.ServerSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Optional;


/**
 * Created by dionis on 04/06/15.
 */
@Service
public class ServerSettingServiceImpl implements ServerSettingsService {
    @Resource
    ServerSettingRepository serverSettingRepository;

    @Transactional
    @Override
    public void save(ServerSettingDTO serverSettingDTO) {
        Assert.notNull(serverSettingDTO);
        ServerSetting to = ServerSettingDTO.to(serverSettingDTO);
        serverSettingRepository.saveAndFlush(to);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ServerSettingDTO> getById(ServerSetting.Id id) {
        Assert.notNull(id);
        return Optional.ofNullable(ServerSettingDTO.from(serverSettingRepository.findOne(id)));
    }
}
