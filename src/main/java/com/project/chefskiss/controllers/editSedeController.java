package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class editSedeController {
    @GetMapping(path = "/editSede", params = {"coord", "idR"})
    public ModelAndView deleteSede(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("coord") String Coord
    ) {
        ModelAndView page = new ModelAndView("editSedePage");
        User utente;
        Sede sede;

        //Lettura dei cookie dell'utente
        if (userData.isEmpty()) {
            System.out.println("No cookies, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        } else {
            utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }

        //Accesso al db per cancellazione sede
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try {
            SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);

            sede = sessionSedeDAO.findByCoordinate(Coord);

            page.addObject("sede", sede);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        DatabaseDAO.closeTransaction();

        return page;
    }

    @PostMapping(path = "/editSede", params = {"via", "citta", "nposti", "coord", "idR"})
    public ModelAndView postAddSede(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("via") String Via,
            @RequestParam("citta") String Citta,
            @RequestParam("nposti") int NPosti,
            @RequestParam("coord") String Coord,
            @RequestParam("idR") int IDRisto
    ){
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente;
        Sede sede = new Sede();

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

        //Accesso al db per l'insermento della sede
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);

        try {
            sede.setVia(Via);
            sede.setCitta(Citta);
            sede.setPosti(NPosti);
            sede.setID_Ristorante(IDRisto);
            sede.setCoordinate(Coord);

            sessionSedeDAO.update(sede);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/restaurant?id="+IDRisto);
    }
}
