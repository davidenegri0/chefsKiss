package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class addSedeController {
    @GetMapping(path = "/addRistorante&Sede")
    public ModelAndView viewAddRisto(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        ModelAndView page = new ModelAndView("addRistoPage");
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

        if (!utente.isRistoratore()){
            System.out.println("User is not Ristoratore, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        }

        return page;
    }

    @PostMapping(path = "/addRistorante&Sede", params = {"nome_risto", "via", "n_civ", "citta", "nposti"})
    public ModelAndView postAddRisto(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam(name = "nome_risto") String NomeRisto,
            @RequestParam(name = "via") String Via,
            @RequestParam(name = "n_civ") int NCiv,
            @RequestParam(name = "citta") String Citta,
            @RequestParam(name = "nposti") int NPosti,
            @RequestParam(name = "coord") String Coord
    ){
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente;
        Via = Via + " " + NCiv;
        Ristorante risto = null;
        List<Piatto> piatti = new ArrayList<>();

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

        // Connesione al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try{
            RistoranteDAO sessionRistoDAO = DatabaseDAO.getRistoDAO(null);
            risto = sessionRistoDAO.create(NomeRisto, utente.getCF());

            SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);

            sessionSedeDAO.create(Coord, Via, Citta, NPosti, risto, piatti);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
            return Utility.redirect(page, "/addRistorante&Sede");
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/restaurant?id="+risto.getID_Ristorante());
    }

    @GetMapping(path = "/addSede")
    public ModelAndView viewAddSede(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        ModelAndView page = new ModelAndView("addSedePage");
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

        //Accesso al db per l'ottenimento del ID ristorante
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        RistoranteDAO sessionRistoDAO = DatabaseDAO.getRistoDAO(null);
        Ristorante Risto = sessionRistoDAO.findByRistoratore(utente.getCF());

        DatabaseDAO.closeTransaction();

        page.addObject("Risto", Risto);

        return page;
    }

    @PostMapping(path = "/addSede", params = {"via", "n_civ", "citta", "nposti", "idristo", "coord"})
    public ModelAndView postAddSede(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("via") String Via,
            @RequestParam("n_civ") int NCiv,
            @RequestParam("citta") String Citta,
            @RequestParam("nposti") int NPosti,
            @RequestParam("idristo") int IDRisto,
            @RequestParam("coord") String Coord
    ){
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente;
        List<Piatto> piatti = new ArrayList<>();

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

        //Vecchio metodo di coordinate per debug
/*
        do {
            Coord = "Coord"+Math.round(Math.random()*2000);
        } while (sessionSedeDAO.findByCoordinate(Coord)==null);
 */
        Via = Via.concat(" "+NCiv);

        try {
            RistoranteDAO sessionRistorante = DatabaseDAO.getRistoDAO(null);
            Ristorante Risto = sessionRistorante.findById(IDRisto);

            sessionSedeDAO.create(Coord, Via, Citta, NPosti, Risto, piatti);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/restaurant?id="+IDRisto);
    }
}
