package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class registrationController {
    @RequestMapping(value = "/registration")
    public ModelAndView showRegistration(
    ){
        ModelAndView page = new ModelAndView("registrationPage");
        System.out.println("Ciao");
        return page;
    }
}
