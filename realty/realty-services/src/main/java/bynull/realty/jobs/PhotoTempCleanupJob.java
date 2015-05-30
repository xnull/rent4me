package bynull.realty.jobs;

import bynull.realty.services.api.PhotoTempService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
public class PhotoTempCleanupJob implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoTempCleanupJob.class);

    @Resource
    PhotoTempService photoTempService;

    @Scheduled(
            cron = "0 0 * * * *" //start every hour
//            initialDelay = 60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        LOGGER.info("Starting to collect garbage");
        photoTempService.collectGarbage();
        LOGGER.info("Ended collecting garbage");
    }
}
