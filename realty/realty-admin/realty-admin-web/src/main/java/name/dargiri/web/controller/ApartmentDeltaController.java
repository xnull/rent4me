package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentInfoDeltaDTO;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import bynull.realty.services.api.ApartmentInfoDeltaService;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.CityService;
import bynull.realty.services.api.CountryService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.ApartmentAdminConverter;
import name.dargiri.web.converters.ApartmentInfoDeltaAdminConverter;
import name.dargiri.web.converters.CityAdminConverter;
import name.dargiri.web.converters.CountryAdminConverter;
import name.dargiri.web.form.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/apartment_deltas")
public class ApartmentDeltaController {


    @Resource
    ApartmentInfoDeltaService apartmentInfoDeltaService;

    @Resource
    ApartmentInfoDeltaAdminConverter apartmentInfoDeltaConverter;

    @RequestMapping(value = {"list/new"})
    public ModelAndView listNew() {
        ModelAndView mav = new ModelAndView("apartment_deltas/list");

        List<? extends ApartmentInfoDeltaDTO> deltaDTOs = apartmentInfoDeltaService.listAllGroupedByApartments(Collections.emptySet());

        List<? extends ApartmentInfoDeltaForm> deltas = apartmentInfoDeltaConverter.toTargetList(deltaDTOs);

        mav.addObject("deltas", deltas);

        return mav;
    }

    @RequestMapping(value = {"list/applied"})
    public ModelAndView listApplied() {
        ModelAndView mav = new ModelAndView("apartment_deltas/list");

        List<? extends ApartmentInfoDeltaDTO> deltaDTOs = apartmentInfoDeltaService.listAllGroupedByApartments(Collections.singleton(ApartmentInfoDeltaService.ListMode.APPLIED));

        List<? extends ApartmentInfoDeltaForm> deltas = apartmentInfoDeltaConverter.toTargetList(deltaDTOs);

        mav.addObject("deltas", deltas);

        return mav;
    }

    @RequestMapping(value = {"list/rejected"})
    public ModelAndView listRejected() {
        ModelAndView mav = new ModelAndView("apartment_deltas/list");

        List<? extends ApartmentInfoDeltaDTO> deltaDTOs = apartmentInfoDeltaService.listAllGroupedByApartments(Collections.singleton(ApartmentInfoDeltaService.ListMode.REJECTED));

        List<? extends ApartmentInfoDeltaForm> deltas = apartmentInfoDeltaConverter.toTargetList(deltaDTOs);

        mav.addObject("deltas", deltas);

        return mav;
    }

    @RequestMapping(value = "/review_delta/{id}")
    public ModelAndView reviewDelta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<ApartmentInfoDeltaDTO> dto = apartmentInfoDeltaService.findById(id);
        if (dto.isPresent()) {
            ModelAndView mav = new ModelAndView("apartment_deltas/edit_form");
            ApartmentInfoDeltaForm form = apartmentInfoDeltaConverter.toTargetType(dto.get()).orElse(null);
            mav.addObject("form", form);
            return mav;
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "Delta not found");
            return new ModelAndView("redirect:/secure/apartment_deltas/list/new");
        }
    }

    @RequestMapping(value = "/approve_delta/{id}")
    public ModelAndView approveDelta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<ApartmentInfoDeltaDTO> dto = apartmentInfoDeltaService.findById(id);
        if (dto.isPresent()) {
            apartmentInfoDeltaService.applyAllDeltasUntilDateIncludingSpecified(id);
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Delta applied");
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "Delta not found");
        }
        return new ModelAndView("redirect:/secure/apartment_deltas/list/new");
    }

    @RequestMapping(value = "/reject_delta/{id}")
    public ModelAndView rejectDelta(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<ApartmentInfoDeltaDTO> dto = apartmentInfoDeltaService.findById(id);
        if (dto.isPresent()) {
            apartmentInfoDeltaService.rejectAllDeltasUntilDateIncludingSpecified(id);
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "Delta rejected");
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "Delta not found");
        }
        return new ModelAndView("redirect:/secure/apartment_deltas/list/new");
    }

}
