package com.project.chefskiss.controllers;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

@Controller
public class registrationController {
    @RequestMapping(value = "/registration")
    public ModelAndView showRegistrationComplete(
            HttpServletResponse response,
            @RequestParam(name = "nome", defaultValue = "", required = false) String Nome,
            @RequestParam(name = "cognome", defaultValue = "", required = false) String Cognome,
            @RequestParam(name = "cf", defaultValue = "", required = false) String CF,
            @RequestParam(name = "email", defaultValue = "", required = false) String Email,
            @RequestParam(name = "telefono", defaultValue = "", required = false) String Telefono,
            @RequestParam(name = "nascita", defaultValue = "", required = false) String Data,
            @RequestParam(name = "pssw", defaultValue = "", required = false) String Password
            ){
        // Variabli
        ModelAndView page;
        User utente;

        // Controllo parametri

        if (Nome.equals("")){
            page = new ModelAndView("registrationPage");
            return page;
        }

        page = new ModelAndView("index");
        //Controllo Data ---> debug
        //System.out.println(Data);

        // Si prova a fetchare i dati dal DB
        try {
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            utente = sessionUserDAO.create(CF, Nome, Cognome, Date.valueOf(Data), Email, Password, Telefono, Date.valueOf(LocalDate.now()),
                    false, false, false, null, false, false, false, null);
            DatabaseDAO.commitTransaction();
            DatabaseDAO.closeTransaction();
        } catch (Exception e){
            //System.out.println(e.getMessage());
            e.printStackTrace();
            page.setViewName("registrationPage");
            page.addObject("errorCode", 1);
            return page;
        }

        //Creazione cookie utente
        try {
            DAOFactory CookieDAO = DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response);
            UserDAO userCookie = CookieDAO.getUserDAO(response);
            userCookie.create(
                    utente.getCF(),
                    utente.getNome(),
                    utente.getCognome(),
                    utente.getD_Nascita(),
                    utente.getEmail(),
                    "redacted",
                    utente.getN_Telefono(),
                    utente.getD_Iscrizione(),
                    false,
                    false,
                    false,
                    "undefined",
                    false,
                    false,
                    false,
                    null
            );
        } catch (UserAlreadyKnownException e){
            System.out.println("Come minchia è possibile?");
        }

        page.addObject("user", utente);
        return page;
    }
}
