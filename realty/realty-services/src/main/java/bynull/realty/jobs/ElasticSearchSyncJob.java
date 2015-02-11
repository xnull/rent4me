package bynull.realty.jobs;

import bynull.realty.services.api.FacebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
//@Component
public class ElasticSearchSyncJob implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchSyncJob.class);

    @Resource
    FacebookService facebookService;

    @Scheduled(
            cron = "0 */5 * * * *" //start each five minutes
//            ,initialDelay = 20*1000, fixedDelay = 60*60*1000
            //start each hour, default delay - one minute
    )
    @Override
    public void run() {
        LOGGER.info("Starting to sync ES");
        LOGGER.info("ES sync job disabled.");
//        facebookService.syncElasticSearchWithDB();
        LOGGER.info("Ended sync of ES");
    }
}
