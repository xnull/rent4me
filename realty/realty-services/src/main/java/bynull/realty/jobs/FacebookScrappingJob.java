package bynull.realty.jobs;

import bynull.realty.services.api.FacebookScrapingPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
@Component
public class FacebookScrappingJob implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookScrappingJob.class);

    @Resource
    FacebookScrapingPostService facebookScrapingPostService;

    @Scheduled(
            cron = "0 */30 * * * *" //start each 30 minutes
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        LOGGER.info("Starting to scrap new FB posts");
        facebookScrapingPostService.scrapNewPosts();
        LOGGER.info("Ended scraping new FB posts");
    }
}