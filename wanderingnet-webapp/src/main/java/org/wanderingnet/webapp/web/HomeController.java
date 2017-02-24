package org.wanderingnet.webapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by guillermoblascojimenez on 29/02/16.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

}
