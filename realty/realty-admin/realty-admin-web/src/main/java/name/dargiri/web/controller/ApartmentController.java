package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentInfoDeltaDTO;
import bynull.realty.services.api.ApartmentInfoDeltaService;
import bynull.realty.services.api.ApartmentService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.ApartmentAdminConverter;
import name.dargiri.web.converters.ApartmentInfoDeltaAdminConverter;
import name.dargiri.web.form.ApartmentForm;
import name.dargiri.web.form.ApartmentInfoDeltaForm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/secure/apartments")
public class ApartmentController {


    @Resource
    ApartmentService apartmentService;

    @Resource
    ApartmentAdminConverter apartmentAdminConverter;

    @RequestMapping(value = {"list"})
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "0") int page) {
        ModelAndView mav = new ModelAndView("apartments/list");

        List<? extends ApartmentDTO> apartmentDTOs = apartmentService.listAll(new PageRequest(page, 500, Sort.Direction.DESC, "logicalCreated"));

        List<? extends ApartmentForm> apartments = apartmentAdminConverter.toTargetList(apartmentDTOs);

        mav.addObject("apartments", apartments);

        return mav;
    }

}
