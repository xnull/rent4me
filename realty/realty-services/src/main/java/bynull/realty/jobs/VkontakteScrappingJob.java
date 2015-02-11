package bynull.realty.jobs;

import bynull.realty.services.api.VkontakteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
@Slf4j
//@Component
public class VkontakteScrappingJob implements Runnable {

    @Resource
    VkontakteService vkontakteService;

    @Scheduled(
            cron = "0 */15 * * * *" //start each 15 minutes
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        log.info("Starting to scrap new VK posts");
        vkontakteService.syncWithVK();
        log.info("Ended scraping new VK posts");
    }
}
