package com.project.chefskiss.controllers;

import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homepageController {
    @RequestMapping(value = "/homepage")
    public ModelAndView homepageLoader(
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    )
    {
        ModelAndView page = new ModelAndView("homepage");

        if(!UserData.isEmpty()){
            User utente = User.decodeUserData(UserData);
            page.addObject("user", utente.getNome()+" "+utente.getCognome());
        }
        else
        {
            System.out.println("No cookies :C");
        }

        return page;
    }
}
