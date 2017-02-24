package org.wanderingnet.webapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.wanderingnet.model.User;
import org.wanderingnet.service.user.UserService;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Controller
public class LoginController extends SessionController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (isAutenticated()) {
            return "redirect:/explore";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam(value = "username", required = true) String email,
                         @RequestParam(value = "name", required = true) String name,
                         @RequestParam(value = "password", required = true) String password,
                         @RequestParam(value = "password-repeated", required = true) String passwordRepeated) {
        if (isAutenticated()) {
            return "redirect:/explore";
        } else {
            User user = userService.registerUser(email, name, password.toCharArray());
            return "redirect:/explore";
        }
    }

}
