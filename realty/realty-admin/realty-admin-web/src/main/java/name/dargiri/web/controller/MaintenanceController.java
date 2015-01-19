package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.services.api.FacebookService;
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
}
