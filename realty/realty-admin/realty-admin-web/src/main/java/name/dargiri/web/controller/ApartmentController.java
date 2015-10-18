package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.impl.blacklist.BlacklistServiceImpl;
import lombok.extern.slf4j.Slf4j;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.ApartmentAdminConverter;
import name.dargiri.web.form.ApartmentForm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dionis on 2/3/14.
 */
@Slf4j
@Controller
@RequestMapping("/secure/apartments")
public class ApartmentController {


    @Resource
    ApartmentService apartmentService;

    @Resource
    private BlacklistServiceImpl blacklistService;

    @Resource
    ApartmentAdminConverter apartmentAdminConverter;

    @RequestMapping(value = {"list"})
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "0") int page) {
        log.trace("Find apartments");

        ModelAndView mav = new ModelAndView("apartments/list");

        List<? extends ApartmentDTO> apartmentDTOs = apartmentService.listAll(
                new PageRequest(page, 100, Sort.Direction.DESC, "logicalCreated")
        );

        List<? extends ApartmentForm> apartments = apartmentAdminConverter.toTargetList(apartmentDTOs);

        mav.addObject("apartments", apartments);
        mav.addObject("page", page);

        return mav;
    }

    @RequestMapping(value = "show/{id}")
    public ModelAndView showApartmentInSearch(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        apartmentService.showApartmentInSearch(id);
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Apartment shown in search");
        return new ModelAndView("redirect:/secure/apartments/list");
    }

    @RequestMapping(value = "hide/{id}")
    public ModelAndView hideApartmentFromSearch(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        apartmentService.hideApartmentFromSearch(id);
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Apartment hidden from search");
        return new ModelAndView("redirect:/secure/apartments/list");
    }

    @RequestMapping(value = "block/{id}")
    public ModelAndView blockApartmentFromSearch(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        blacklistService.addApartmentToBlacklist(id);
        apartmentService.saveIdents(id);
        redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Apartment: " + id +", have been added to blacklist");
        return new ModelAndView("redirect:/secure/apartments/list");
    }
}
