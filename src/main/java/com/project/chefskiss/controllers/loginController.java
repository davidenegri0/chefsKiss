package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class loginController {
    @RequestMapping(value = "/login")
    public ModelAndView showLogin(
            @RequestParam(name = "usr", defaultValue = "",required = false) String user,
            @RequestParam(name = "pssw", defaultValue = "", required = false) String password
    )
    {
        ModelAndView page = new ModelAndView("loginPage");

        if (user.equals("Davide") && password.equals("Segretissima")){
            System.out.println("Logged in");
            page.addObject("loginStatus", "true");
        }
        else {
            System.out.println("Failed Logged in");
            page.addObject("loginStatus", "false");
        }

        return page;
    }
}
