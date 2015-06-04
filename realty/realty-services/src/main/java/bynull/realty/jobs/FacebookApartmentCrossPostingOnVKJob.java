package bynull.realty.jobs;

import bynull.realty.services.api.ApartmentService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author dionis on 06/12/14.
 */
@Slf4j
public class FacebookApartmentCrossPostingOnVKJob implements Runnable {

    @Resource
    JobHelperComponent jobHelperComponent;

    @Resource
    ApartmentService apartmentService;

    @Scheduled(
            cron = "0 0,15,30,45 * * * *" //start on each 9-th minute of each hour
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        log.info(">>> Starting Cross-posting FB apartments to VK");
        apartmentService.publishFBApartmentsOnVkPage(new DateTime().minusMinutes(15).toDate(), new Date());
        log.info("<<< Finished Cross-posting FB apartments to VK");
        /*jobHelperComponent.addJob(new JobHelperComponent.JobAcceptanceCallback() {
            @Override
            public void onJobAdded(JobHelperComponent.Job job) {
                log.info("Automatic job " + FacebookApartmentCrossPostingOnVKJob.class + " accepted");
            }

            @Override
            public void onJobRejected(JobHelperComponent.Job job) {
                log.info("Automatic job " + FacebookApartmentCrossPostingOnVKJob.class + " rejected");
            }
        }, );
        */
    }
}
