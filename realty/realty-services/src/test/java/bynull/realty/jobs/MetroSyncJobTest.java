package bynull.realty.jobs;

import bynull.realty.ServiceTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class MetroSyncJobTest extends ServiceTest {

    @Autowired
    private MetroSyncJob metroSyncJob;

    @Ignore("It work fine, but it's not necessary to run it every time")
    @Test
    @Rollback(value = false)
    public void testRun() throws Exception {
        metroSyncJob.run();
    }
}