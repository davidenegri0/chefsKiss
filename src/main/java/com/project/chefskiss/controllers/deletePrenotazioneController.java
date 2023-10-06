package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deletePrenotazioneController {
    @GetMapping (path = "/deletePrenotazione")
    public ModelAndView DeletePrenotazione (
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") Integer id
    ) {
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

        PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);

        Prenotazione prenotazione = prenotazioneDAO.findById(id);
        prenotazioneDAO.delete(prenotazione);
        DatabaseDAO.commitTransaction();

        page = Utility.redirect(page, "/prenotazioniList?id=" + utente.getCF());

        return page;
    }
}
