package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.services.api.FacebookService;
import bynull.realty.services.api.MetroService;
import bynull.realty.services.api.VkontakteService;
import bynull.realty.services.metro.MetroServiceException;
import name.dargiri.web.Constants;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/maintenance")
public class MaintenanceController implements InitializingBean, DisposableBean{

    @Resource
    FacebookService facebookService;

    @Resource
    VkontakteService vkontakteService;

    @Resource
    MetroService metroService;

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

    @RequestMapping(value = "")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("maintenance/index");
        modelAndView.addObject("activeJobName", syncJobName.get());
        return modelAndView;
    }

    final AtomicBoolean syncInProgress = new AtomicBoolean();
    final AtomicReference<String> syncJobName = new AtomicReference<>();

    private abstract class Job implements Runnable {
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
                action();
            } finally {
                syncInProgress.set(false);
                syncJobName.set(null);
            }
        }

        protected abstract void action();
    }

    private ModelAndView startJob(RedirectAttributes redirectAttributes, Job job) {
        boolean set = syncInProgress.compareAndSet(false, true);
        if(set) {
            syncJobName.set(job.getName());
            jobRunner.submit(job);
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Started job: "+job.getName());
        } else {
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Sync already in progress");
        }
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "reparse_existing_fb_posts")
    public ModelAndView reparseExistingFBPosts(RedirectAttributes redirectAttributes) {
        String jobName = "Re-Parsing of Existing FB Posts";
        return startJob(redirectAttributes, new Job(jobName) {
            @Override
            protected void action() {
                facebookService.reparseExistingFBPosts();
            }
        });
    }

    @RequestMapping(value = "reparse_existing_vk_posts")
    public ModelAndView reparseExistingVKPosts(RedirectAttributes redirectAttributes) {
        String jobName = "Re-Parsing of Existing VK Posts";
        return startJob(redirectAttributes, new Job(jobName) {
            @Override
            protected void action() {
                vkontakteService.reparseExistingVKPosts();
            }
        });
    }

    @RequestMapping(value = "manual_sync_fb")
    public ModelAndView manualSyncWithFB(RedirectAttributes redirectAttributes) {
        String jobName = "Sync with FB";
        return startJob(redirectAttributes, new Job(jobName) {
            @Override
            protected void action() {
                facebookService.syncWithFB();
            }
        });
    }

    @RequestMapping(value = "manual_sync_vk")
    public ModelAndView manualSyncWithVK(RedirectAttributes redirectAttributes) {
        String jobName = "Sync with VK";
        return startJob(redirectAttributes, new Job(jobName) {
            @Override
            protected void action() {
                vkontakteService.syncWithVK();
            }
        });
    }

    @RequestMapping(value = "manual_sync_metros")
    public ModelAndView manualSyncMetros(RedirectAttributes redirectAttributes) {
        String jobName = "Sync metros";
        return startJob(redirectAttributes, new Job(jobName) {
            @Override
            protected void action() {
                try {
                    metroService.syncMoscowMetrosWithDatabase();
                } catch (MetroServiceException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
