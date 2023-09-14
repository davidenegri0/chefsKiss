package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homepageController {
    @RequestMapping(value = "/homepage")
    public ModelAndView homepageLoader(
            @CookieValue("loggedUser") String UserData
    )
    {
        ModelAndView page = new ModelAndView("homepage");

        //TODO: Controllo Cookie

        return page;
    }
}
