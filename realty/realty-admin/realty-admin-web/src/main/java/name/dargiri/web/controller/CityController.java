package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import bynull.realty.services.api.CityService;
import bynull.realty.services.api.CountryService;
import name.dargiri.web.Constants;
import name.dargiri.web.converters.CityAdminConverter;
import name.dargiri.web.converters.CountryAdminConverter;
import name.dargiri.web.form.BoundingBoxForm;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.CountryForm;
import name.dargiri.web.form.GeoPointForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/cities")
public class CityController {
    private static final String RUSSIA = "Россия";

    @Resource
    CityService cityService;

    @Resource
    CountryService countryService;

    @Resource
    CityAdminConverter cityConverter;

    @Resource
    CountryAdminConverter countryAdminConverter;

    @RequestMapping(value = {"", "/all"})
    public ModelAndView all() {
        CountryDTO country = getSelectedCountry();
        ModelAndView mav = new ModelAndView("cities/list");
        List<? extends CityDTO> cities = cityService.findByCountry(country);
        List<? extends CityForm> all = cityConverter.toTargetList(cities);
        mav.addObject("cities", all);
        return mav;
    }

    @RequestMapping(value = "/new")
    public ModelAndView createForm()

    {
        CountryDTO selectedCountry = getSelectedCountry();
        CountryForm countryForm = countryAdminConverter.toTargetType(selectedCountry).orElse(null);
        CityForm cityForm = new CityForm();
        BoundingBoxForm area = new BoundingBoxForm();
        area.setHigh(new GeoPointForm());
        area.setLow(new GeoPointForm());
        cityForm.setArea(area);
        cityForm.setCountry(countryForm);

        ModelAndView mav = new ModelAndView("cities/edit_form");
        mav.addObject("city", cityForm);
        return mav;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ModelAndView create(CityForm cityForm, RedirectAttributes redirectAttributes)

    {
        CityDTO city = cityConverter.toSourceType(cityForm);
        boolean created = cityService.create(city);
        if(created) {
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "City added");
            return new ModelAndView("redirect:/secure/cities/all");
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "City not added");
            ModelAndView mav = new ModelAndView("cities/edit_form");
            mav.addObject("city", cityForm);
            return mav;
        }
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView editForm(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<CityDTO> dto = cityService.findById(id);
        if (dto.isPresent()) {
            ModelAndView mav = new ModelAndView("cities/edit_form");
            CityForm cityForm = cityConverter.toTargetType(dto.get()).orElse(null);
            mav.addObject("city", cityForm);
            return mav;
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "City not found");
            return new ModelAndView("redirect:/secure/cities/all");
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public ModelAndView edit(@PathVariable("id") Long id, CityForm cityForm) {
        CityDTO city = cityConverter.toSourceType(cityForm);
        CityDTO updated = cityService.update(city);
        if (updated != null) {
            return new ModelAndView("redirect:/secure/cities/all");
        } else {
            ModelAndView mav = new ModelAndView("main/person");
            mav.addObject("city", city);
            return mav;
        }
    }


    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = cityService.delete(id);
        if(deleted) {
            redirectAttributes.addFlashAttribute(Constants.INFO_MESSAGE, "City deleted");
        } else {
            redirectAttributes.addFlashAttribute(Constants.ERROR_MESSAGE, "City not deleted");
        }
        return new ModelAndView("redirect:/secure/cities/all");
    }


    private CountryDTO getSelectedCountry() {
        //hard-code russia for now
        return countryService.findByName(RUSSIA);
    }
}
