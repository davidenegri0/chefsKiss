package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
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
        User utente = new User();

        // Controllo parametri

        if (Nome.equals("")){
            page = new ModelAndView("registrationPage");
            return page;
        }

        page = new ModelAndView("homepage");
        //Controllo Data ---> debug
        System.out.println(Data);

        // Si prova a fatchare i dati dal DB
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
            return page;        //TODO: Handle dell'errore di registrazione
        }


        page.addObject("user", utente.getNome()+" "+utente.getCognome());
        return page;
    }
}
