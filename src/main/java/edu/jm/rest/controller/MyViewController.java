package edu.jm.rest.controller;

import edu.jm.rest.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyViewController {

    @GetMapping(value = "/users")
    public String users(UsernamePasswordAuthenticationToken token, ModelMap model){
        User user = (User) token.getPrincipal();
        model.addAttribute("user", user);
        return "users_page";
    }
}
