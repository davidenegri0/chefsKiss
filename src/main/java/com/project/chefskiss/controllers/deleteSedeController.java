package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.dataAccessObjects.ValutazioneDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deleteSedeController {
    @GetMapping(path = "/deleteSede", params = {"coord", "idR"})
    public ModelAndView deleteSede(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("coord") String Coord,
            @RequestParam("idR") int IDR
    ){
        ModelAndView page = new ModelAndView();
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

        //Accesso al db per cancellazione sede
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try {
            SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);
            ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
            PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);

            Sede s = new Sede();
            s.setCoordinate(Coord);
            valutazioneDAO.deleteBySede(Coord);
            prenotazioneDAO.deleteBySede(Coord);
            sessionSedeDAO.delete(s);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page,"/restaurant?id="+IDR);
    }
}
