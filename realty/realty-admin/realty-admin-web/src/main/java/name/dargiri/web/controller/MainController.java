package name.dargiri.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author dionis on 05/05/14.
 */
@Controller
@RequestMapping("")
public class MainController {
    @RequestMapping("")
    public String home() {
        return "redirect:/secure/people";
    }

    @RequestMapping("/login")
    public ModelAndView login(ModelMap modelMap) {
        return new ModelAndView("login");
    }

    @RequestMapping("/loginfailed")
    public ModelAndView loginerror(ModelMap model) {
        model.addAttribute("error", "true");
        return new ModelAndView("login");

    }

    @RequestMapping("/logout")
    public ModelAndView logout(ModelMap model) {
        return new ModelAndView("login");
    }
}
