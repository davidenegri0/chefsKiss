package com.project.chefskiss.controllers;

import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class profileController {
    @RequestMapping(value = "profile")
    public ModelAndView onProfileViewRequest(
            HttpServletResponse response,
            @CookieValue("loggedUser") String userData
    ){
        ModelAndView page = new ModelAndView("profilePage");

        User utente = User.decodeUserData(userData);
        page.addObject("utente", utente);

        return page;
    }
}
