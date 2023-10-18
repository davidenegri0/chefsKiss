package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deleteProfile {
    @GetMapping (path = "/deleteProfile")
    public ModelAndView DeleteProfile (
            HttpServletResponse response,
            @CookieValue (value = "loggedUser", defaultValue = "") String userData
    ){
        ModelAndView page = new ModelAndView("index");
        User utente;

        //Lettura dei cookie dell'utente
        if (userData.isEmpty()) {
            System.out.println("No cookies, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        } else {
            utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        UserDAO userDAO = DatabaseDAO.getUserDAO(null);

        try {
            userDAO.delete(utente);
            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        DAOFactory CookieDAO = DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response);
        UserDAO userDAO1 = CookieDAO.getUserDAO(response);
        try {
            userDAO1.delete(null);
        }  catch (RuntimeException e){
            e.printStackTrace();
            CookieDAO.rollbackTransaction();
            CookieDAO.closeTransaction();
        }

        page = Utility.redirect(page, "/homepage");

        return page;
    }
}
