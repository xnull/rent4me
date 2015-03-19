package bynull.realty.jobs;

import bynull.realty.services.api.FacebookService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author dionis on 06/12/14.
 */
@Slf4j
//@Component
public class TestJob implements Runnable {

    @Scheduled(
            cron = "0 * * * * *" //start each 30 minutes
//            initialDelay = 60*60*1000, fixedDelay = 60*60*1000
    )//start each hour, default delay - one minute
    @Override
    public void run() {
        log.info("Starting test job");
    }
}
