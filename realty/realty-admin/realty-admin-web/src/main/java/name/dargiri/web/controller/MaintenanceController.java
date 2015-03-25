package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.jobs.JobHelperComponent;
import bynull.realty.services.api.FacebookService;
import bynull.realty.services.api.MetroService;
import bynull.realty.services.api.VkontakteService;
import name.dargiri.web.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/maintenance")
public class MaintenanceController {

    @Resource
    FacebookService facebookService;

    @Resource
    VkontakteService vkontakteService;

    @Resource
    MetroService metroService;

    @Resource
    JobHelperComponent jobHelperComponent;

    @RequestMapping(value = "")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("maintenance/index");
        if(jobHelperComponent.getSyncJobName().isPresent()) {
            modelAndView.addObject("activeJobName", jobHelperComponent.getSyncJobName().get());
        }
        return modelAndView;
    }


    @RequestMapping(value = "reparse_existing_fb_posts")
    public ModelAndView reparseExistingFBPosts(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.reparseExistingFBPosts());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "reparse_existing_vk_posts")
    public ModelAndView reparseExistingVKPosts(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.reparseExistingVKPosts());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_fb")
    public ModelAndView manualSyncWithFB(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.syncWithFB());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_vk")
    public ModelAndView manualSyncWithVK(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.manualSyncWithVK());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_moscow_metros")
    public ModelAndView manualSyncMetros(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.manualSyncMoscowMetros());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_st_petersburg_metros")
    public ModelAndView manualSyncStPetersburgMetros(RedirectAttributes redirectAttributes) {
        jobHelperComponent.addJob(new MaintenanceControllerJobAcceptanceCallback(redirectAttributes), jobHelperComponent.manualSyncStPetersburgMetros());
        return new ModelAndView("redirect:/secure/maintenance");
    }

    private class MaintenanceControllerJobAcceptanceCallback implements JobHelperComponent.JobAcceptanceCallback{

        private final RedirectAttributes redirectAttributes;

        public MaintenanceControllerJobAcceptanceCallback(RedirectAttributes redirectAttributes) {
            this.redirectAttributes = redirectAttributes;
        }

        @Override
        public void onJobAdded(JobHelperComponent.Job job) {
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Started job: "+job.getName());
        }

        @Override
        public void onJobRejected(JobHelperComponent.Job job) {
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Sync already in progress");
        }
    }
}
