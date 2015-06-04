package bynull.realty.dao;

import bynull.realty.data.business.configs.ServerSetting;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dionis on 04/06/15.
 */
public interface ServerSettingRepository extends JpaRepository<ServerSetting, ServerSetting.Id> {
}
