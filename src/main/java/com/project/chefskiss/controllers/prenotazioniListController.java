package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class prenotazioniListController {
    @RequestMapping(path = "/prenotazioniList")
    public ModelAndView viewPrenotazioniList (
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        ModelAndView page = new ModelAndView("prenotazioniListPage");
        User utente;

        //Lettura dei cookie dell'utente
        if(userData.isEmpty()){
            System.out.println("No cookies, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        }
        else
        {
            utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);
        List<Prenotazione> prenotazioni = prenotazioneDAO.findByUser(utente.getCF());

        page.addObject("prenotazioni", prenotazioni);

        return page;
    }

}
