package bynull.realty.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
@Slf4j
public class ApartmentCleanUpJob implements Runnable {

    @Resource
    JobHelperComponent jobHelperComponent;

    @Scheduled(
            cron = "0 9 * * * *" //start on each 9-th minute of each hour
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        jobHelperComponent.addJob(new JobHelperComponent.JobAcceptanceCallback() {
            @Override
            public void onJobAdded(JobHelperComponent.Job job) {
                log.info("Automatic job " + FacebookScrappingJob.class + " accepted");
            }

            @Override
            public void onJobRejected(JobHelperComponent.Job job) {
                log.info("Automatic job " + FacebookScrappingJob.class + " rejected");
            }
        }, jobHelperComponent.unPublishOldNonInternalApartments());
    }
}
