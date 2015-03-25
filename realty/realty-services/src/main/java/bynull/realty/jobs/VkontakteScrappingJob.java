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
@Component
public class VkontakteScrappingJob implements Runnable {

    @Resource
    JobHelperComponent jobHelperComponent;

    @Scheduled(
            cron = "0 20,50 * * * *" //start each 30 minutes
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        jobHelperComponent.addJob(new JobHelperComponent.JobAcceptanceCallback() {
            @Override
            public void onJobAdded(JobHelperComponent.Job job) {
                log.info("Automatic job " + VkontakteScrappingJob.class + " accepted");
            }

            @Override
            public void onJobRejected(JobHelperComponent.Job job) {
                log.info("Automatic job " + VkontakteScrappingJob.class + " rejected");
            }
        }, jobHelperComponent.syncWithFB());
    }
}
