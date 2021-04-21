package edu.jm.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyViewController {

    @GetMapping(value = "/users")
    public String users(){
        return "users_page";
    }
}
