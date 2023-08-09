package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homepageController {
    @RequestMapping(name = "/homepage")
    public ModelAndView homepageLoader()
    {
        ModelAndView page = new ModelAndView("homepage");



        return page;
    }
}
