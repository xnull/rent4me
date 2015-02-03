package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.services.api.FacebookService;
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

    @RequestMapping(value = "")
    public ModelAndView index() {
        return new ModelAndView("maintenance/index");
    }

    @RequestMapping(value = "reparse_existing_fb_posts")
    public ModelAndView reparseExistingFBPosts(RedirectAttributes redirectAttributes) {
        facebookService.reparseExistingFBPosts();
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "All FB posts migrated");
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_fb")
    public ModelAndView manualSyncWithFB(RedirectAttributes redirectAttributes) {
        facebookService.syncWithFB();
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Synced manually with FB");
        return new ModelAndView("redirect:/secure/maintenance");
    }

    @RequestMapping(value = "manual_sync_vk")
    public ModelAndView manualSyncWithVK(RedirectAttributes redirectAttributes) {
        vkontakteService.syncWithVK();
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Synced manually with VK");
        return new ModelAndView("redirect:/secure/maintenance");
    }
}
