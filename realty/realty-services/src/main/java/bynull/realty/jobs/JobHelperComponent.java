package bynull.realty.jobs;

import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.FacebookService;
import bynull.realty.services.api.MetroService;
import bynull.realty.services.api.VkontakteService;
import bynull.realty.services.metro.MetroServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by dionis on 3/25/15.
 */
@Component
@Slf4j
public class JobHelperComponent implements InitializingBean, DisposableBean {
    @Resource
    FacebookService facebookService;

    @Resource
    VkontakteService vkontakteService;

    @Resource
    MetroService metroService;

    @Resource
    ApartmentService apartmentService;

    ExecutorService jobRunner;

    @Override
    public void afterPropertiesSet() throws Exception {
        jobRunner = Executors.newSingleThreadExecutor();
    }

    @Override
    public void destroy() throws Exception {
        jobRunner.shutdown();
        jobRunner = null;
    }


    final AtomicBoolean syncInProgress = new AtomicBoolean();
    final AtomicReference<String> syncJobName = new AtomicReference<>();

    public Optional<String> getSyncJobName() {
        return Optional.ofNullable(syncJobName.get());
    }

    public abstract class Job implements Runnable {
        private final String name;

        public Job(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public final void run() {
            try {
                log.info("Starting job "+name);
                action();
            } catch (Exception e){
                log.info("Job ended with exception "+name+"; ", e.getMessage());
            }finally {
                syncInProgress.set(false);
                syncJobName.set(null);
                log.info("Ended job "+name);
            }
        }

        protected abstract void action();
    }

    public static interface JobAcceptanceCallback {
        void onJobAdded(Job job);
        void onJobRejected(Job job);
    }

    public void addJob(JobAcceptanceCallback callback, Job job) {
        boolean set = syncInProgress.compareAndSet(false, true);
        if(set) {
            syncJobName.set(job.getName());
            jobRunner.submit(job);
            callback.onJobAdded(job);
        } else {
            callback.onJobRejected(job);
        };
    }

    public Job reparseExistingFBPosts() {
        String jobName = "Re-Parsing of Existing FB Posts";
        return new Job(jobName) {
            @Override
            protected void action() {
                facebookService.reparseExistingFBPosts();
            }
        };
    }

    public Job reparseExistingVKPosts() {
        String jobName = "Re-Parsing of Existing VK Posts";
        return new Job(jobName) {
            @Override
            protected void action() {
                vkontakteService.reparseExistingVKPosts();
            }
        };
    }

    public Job syncWithFB() {
        String jobName = "Sync with FB";
        return new Job(jobName) {
            @Override
            protected void action() {
                facebookService.syncWithFB();
            }
        };
    }

    public Job manualSyncWithVK() {
        String jobName = "Sync with VK";
        return new Job(jobName) {
            @Override
            protected void action() {
                vkontakteService.syncWithVK();
            }
        };
    }

    public Job manualSyncMoscowMetros() {
        String jobName = "Sync metros Moscow";
        return new Job(jobName) {
            @Override
            protected void action() {
                try {
                    metroService.syncMoscowMetrosWithDatabase();
                } catch (MetroServiceException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public Job manualSyncStPetersburgMetros() {
        String jobName = "Sync metros SPB";
        return new Job(jobName) {
            @Override
            protected void action() {
                try {
                    metroService.syncStPetersburgMetrosWithDatabase();
                } catch (MetroServiceException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public Job unPublishOldNonInternalApartments() {
        String jobName = "Un-publish non-internal apartments";
        return new Job(jobName) {
            @Override
            protected void action() {
                apartmentService.unPublishOldNonInternalApartments();
            }
        };
    }
}
