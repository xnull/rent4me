package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import bynull.realty.services.api.FacebookService;
import bynull.realty.services.api.VkontakteService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.FacebookPageAdminConverter;
import name.dargiri.web.converters.FacebookPostAdminConverter;
import name.dargiri.web.converters.VkontaktePageAdminConverter;
import name.dargiri.web.converters.VkontaktePostAdminConverter;
import name.dargiri.web.form.FacebookPageForm;
import name.dargiri.web.form.FacebookPostForm;
import name.dargiri.web.form.VkontaktePageForm;
import name.dargiri.web.form.VkontaktePostForm;
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
import java.util.Collections;
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
    VkontaktePageAdminConverter vkPageConverter;

    @Resource
    FacebookPostAdminConverter fbPostConverter;

    @Resource
    VkontaktePostAdminConverter vkPostConverter;

    @Resource
    FacebookService facebookService;

    @Resource
    VkontakteService vkontakteService;

    ///////// FB stuff

    @RequestMapping(value = "fb")
    public ModelAndView indexFB() {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_list");
        List<FacebookPageDTO> pages = facebookService.listAllPages();
        List<FacebookPageForm> forms = fbPageConverter.toTargetList(pages);
        mav.addObject("pages", forms);
        return mav;
    }

    @RequestMapping(value = "fb/new")
    public ModelAndView newFBPage() {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_edit");
        FacebookPageForm form = new FacebookPageForm();
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "fb/new", method = RequestMethod.POST)
    public ModelAndView saveNewFBPage(FacebookPageForm facebookPageForm, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page created");

        facebookService.save(fbPageConverter.toSourceType(facebookPageForm));

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/{id}/edit")
    public ModelAndView editFBPage(@PathVariable("id") long fbPageId) {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_page_edit");
        FacebookPageDTO page = facebookService.findPageById(fbPageId);
        FacebookPageForm form = fbPageConverter.toTargetType(page);
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "fb/{id}/edit", method = RequestMethod.POST)
    public ModelAndView updateFBPage(@PathVariable("id") long fbPageId, FacebookPageForm form, RedirectAttributes redirectAttributes) {
//        FacebookPageDTO page = facebookService.findPageById(fbPageId);

        facebookService.save(fbPageConverter.toSourceType(form));

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page updated");

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/{id}/delete")
    public ModelAndView deleteFBPage(@PathVariable("id") long fbPageId, RedirectAttributes redirectAttributes) {


        facebookService.delete(fbPageId);

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Facebook page deleted");

        return new ModelAndView("redirect:/secure/socialnet/fb");
    }

    @RequestMapping(value = "fb/posts")
    public ModelAndView listFbPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "limit", defaultValue = "10") int limit,
                                    @RequestParam(value = "text", required = false) String text) {
        ModelAndView mav = new ModelAndView("socialnet/fb/fb_posts_list");
        long totalElements = facebookService.countByQuery(text);
        PaginationHelper paginationHelper = new PaginationHelper(totalElements, page, limit, "/secure/socialnet/fb/posts");
        List<FacebookPostDTO> posts = facebookService.findPosts(text, new PageRequest(paginationHelper.getCurrentPage() - 1, limit, Sort.Direction.DESC, "created"));
        List<FacebookPostForm> forms = fbPostConverter.toTargetList(posts);
        mav.addObject("paginationHelper", paginationHelper);
        mav.addObject("totalPages", totalElements);
        mav.addObject("searchText", text);
        mav.addObject("page", page);
        mav.addObject("posts", forms);
        return mav;
    }

    /////////// end of FB stuff


    ////////// VK stuff

    @RequestMapping(value = "vk")
    public ModelAndView indexVK() {
        ModelAndView mav = new ModelAndView("socialnet/vk/vk_page_list");
        List<VkontaktePageDTO> pages = vkontakteService.listAllPages();
        List<VkontaktePageForm> forms = vkPageConverter.toTargetList(pages);
        mav.addObject("pages", forms);
        return mav;
    }

    @RequestMapping(value = "vk/new")
    public ModelAndView newVKPage() {
        ModelAndView mav = new ModelAndView("socialnet/vk/vk_page_edit");
        VkontaktePageForm form = new VkontaktePageForm();
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "vk/new", method = RequestMethod.POST)
    public ModelAndView saveNewVKPage(VkontaktePageForm vkontaktePageForm, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "VK page created");

        vkontakteService.save(vkPageConverter.toSourceType(vkontaktePageForm));

        return new ModelAndView("redirect:/secure/socialnet/vk");
    }

    @RequestMapping(value = "vk/{id}/edit")
    public ModelAndView editVKPage(@PathVariable("id") long fbPageId) {
        ModelAndView mav = new ModelAndView("socialnet/vk/vk_page_edit");
        VkontaktePageDTO page = vkontakteService.findPageById(fbPageId);
        VkontaktePageForm form = vkPageConverter.toTargetType(page);
        mav.addObject("page", form);
        return mav;
    }

    @RequestMapping(value = "vk/{id}/edit", method = RequestMethod.POST)
    public ModelAndView updateVKPage(@PathVariable("id") long vkPageId, VkontaktePageForm form, RedirectAttributes redirectAttributes) {
//        FacebookPageDTO page = facebookService.findPageById(fbPageId);

        vkontakteService.save(vkPageConverter.toSourceType(form));

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "VK page updated");

        return new ModelAndView("redirect:/secure/socialnet/vk");
    }

    @RequestMapping(value = "vk/{id}/delete")
    public ModelAndView deleteVKPage(@PathVariable("id") long vkPageId, RedirectAttributes redirectAttributes) {


        vkontakteService.delete(vkPageId);

        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "VK page deleted");

        return new ModelAndView("redirect:/secure/socialnet/vk");
    }

    @RequestMapping(value = "vk/posts")
    public ModelAndView listVKPosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "limit", defaultValue = "10") int limit,
                                    @RequestParam(value = "text", required = false) String text) {
        ModelAndView mav = new ModelAndView("socialnet/vk/vk_posts_list");
        long totalElements = vkontakteService.countByQuery(text);
        PaginationHelper paginationHelper = new PaginationHelper(totalElements, page, limit, "/secure/socialnet/vk/posts");
        //TODO: fix it later
        List<VkontaktePostDTO> posts = Collections.emptyList();//vkontakteService.findPosts(text, new PageRequest(paginationHelper.getCurrentPage() - 1, limit, Sort.Direction.DESC, "created"));
        List<VkontaktePostForm> forms = vkPostConverter.toTargetList(posts);
        mav.addObject("paginationHelper", paginationHelper);
        mav.addObject("totalPages", totalElements);
        mav.addObject("searchText", text);
        mav.addObject("page", page);
        mav.addObject("posts", forms);
        return mav;
    }
}
