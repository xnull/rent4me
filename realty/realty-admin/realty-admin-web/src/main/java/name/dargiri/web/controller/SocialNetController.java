package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.services.api.FacebookService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.FacebookPageAdminConverter;
import name.dargiri.web.converters.FacebookPostAdminConverter;
import name.dargiri.web.form.FacebookPageForm;
import name.dargiri.web.form.FacebookPostForm;
import name.dargiri.web.utils.PaginationHelper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/socialnet")
public class SocialNetController {

    @Resource
    FacebookPageAdminConverter fbPageConverter;

    @Resource
    FacebookPostAdminConverter fbPostConverter;

    @Resource
    FacebookService facebookService;

    @RequestMapping(value = {"fb"})
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_list");
        List<FacebookPageDTO> pages = facebookService.listAllPages();
        List<FacebookPageForm> forms = fbPageConverter.toTargetList(pages);
        mav.addObject("pages", forms);
        return mav;
    }

    @RequestMapping(value = "fb/new")
    public ModelAndView newPage() {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_edit");
        FacebookPageForm form = new FacebookPageForm();
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "fb/new", method = RequestMethod.POST)
    public ModelAndView saveNewPage(FacebookPageForm facebookPageForm, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page updated");

        facebookService.save(fbPageConverter.toSourceType(facebookPageForm));

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/{id}/edit")
    public ModelAndView editPage(@PathVariable("id") long fbPageId) {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_edit");
        FacebookPageDTO page = facebookService.findPageById(fbPageId);
        FacebookPageForm form = fbPageConverter.toTargetType(page);
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "fb/{id}/edit", method = RequestMethod.POST)
    public ModelAndView updatePage(@PathVariable("id") long fbPageId, FacebookPageForm form, RedirectAttributes redirectAttributes) {
//        FacebookPageDTO page = facebookService.findPageById(fbPageId);

        facebookService.save(fbPageConverter.toSourceType(form));

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page updated");

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/{id}/delete")
    public ModelAndView editPage(@PathVariable("id") long fbPageId, RedirectAttributes redirectAttributes) {


        facebookService.delete(fbPageId);

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page deleted");

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/posts")
    public ModelAndView listFbPosts(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_posts_list");
        long totalElements = facebookService.countOfPages();
        PaginationHelper paginationHelper = new PaginationHelper(totalElements, page, limit, "/secure/socialnet/fb/posts");
        List<FacebookPostDTO> posts = facebookService.findPosts(new PageRequest(paginationHelper.getCurrentPage() - 1, limit, Sort.Direction.DESC, "imported"));
        List<FacebookPostForm> forms = fbPostConverter.toTargetList(posts);
        mav.addObject("paginationHelper", paginationHelper);
        mav.addObject("totalPages", totalElements);
        mav.addObject("page", page);
        mav.addObject("posts", forms);
        return mav;
    }
}
