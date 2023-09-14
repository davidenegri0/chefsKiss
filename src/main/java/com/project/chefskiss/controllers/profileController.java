package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class profileController {
    @RequestMapping(value = "profile")
    public ModelAndView onProfileViewRequest(){
        ModelAndView page = new ModelAndView("profilePage");

        //TODO: Roba per la visualizzazione del profilo

        return page;
    }
}
