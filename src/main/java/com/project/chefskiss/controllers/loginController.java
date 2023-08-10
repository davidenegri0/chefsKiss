package com.project.chefskiss.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class loginController {
    private final String root_username = "root_Davide";
    private final String root_password = "the_password_is_root";
    @RequestMapping(value = "/login")
    public ModelAndView showLogin(
            @RequestParam(name = "usr", defaultValue = "",required = false) String user,
            @RequestParam(name = "pssw", defaultValue = "", required = false) String password
    )
    {
        ModelAndView page = new ModelAndView("loginPage");

        if (user.equals(root_username) && password.equals(root_password)){
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
