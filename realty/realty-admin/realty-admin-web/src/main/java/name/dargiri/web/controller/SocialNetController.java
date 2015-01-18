package name.dargiri.web.controller;

//import name.dargiri.data.dto.PersonDTO;
//import name.dargiri.data.service.PersonService;
import bynull.realty.services.api.FacebookScrapingPostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by dionis on 2/3/14.
 */

@Controller
@RequestMapping("/secure/socialnet")
public class SocialNetController {

    @Resource
    FacebookScrapingPostService facebookScrapingPostService;

    @RequestMapping(value = {"fb"})
    public ModelAndView index() {
        return new ModelAndView();
    }


}
