package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.net.http.HttpResponse;

@Controller
public class loginController {

    @RequestMapping(value = "/login")
    public ModelAndView showLogin(
            @RequestParam(name = "email", defaultValue = "",required = false) String email,
            @RequestParam(name = "pssw", defaultValue = "", required = false) String password
            )
    {
        // Variabili
        ModelAndView page = new ModelAndView("loginPage");
        User utente;

        // Controllo dati da login
        if(email.equals("") || password.equals("")){
            System.out.println("Empty fields");
            page.addObject("errorCode", 0);
        }
        else
        {
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            utente = sessionUserDAO.findByEmail(email);
            DatabaseDAO.closeTransaction();
            if (password.equals(utente.getPassword())){
                System.out.println("Logged in");
                page.setViewName("homepage");
                page.addObject("user", utente.getNome()+" "+utente.getCognome());
            }
            else {
                System.out.println("Failed Logged in");
                page.addObject("errorCode", 1);
            }
        }

        return page;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView onLogoutRequest(){
        return new ModelAndView("homepage");
    }
}
