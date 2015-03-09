package bynull.realty.jobs;

import bynull.realty.services.metro.MetroServiceException;
import bynull.realty.services.metro.MoscowMetroSynchronisationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */

@Slf4j
public class MetroSyncJob implements Runnable {

    @Resource
    private MoscowMetroSynchronisationService moscowMetroSynchronisationService;

    @Scheduled(cron = "0 0 0 1 * *")
    @Override
    public void run() {
        log.info("Starting to sync new metro stations");
        try {
            moscowMetroSynchronisationService.syncWithDatabase();
        } catch (MetroServiceException e) {
            log.error("Metro sync error", e);
        }

        log.info("Ended sync new metro stations");
    }
}
