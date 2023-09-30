package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.modelObjects.Ristorante;
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
public class resturantsListController {

    @GetMapping(path = "/resturantsList")
    public ModelAndView viewResturantsList(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        //Variabili
        ModelAndView page = new ModelAndView("resturantsListPage");
        List<Ristorante> ristoranti;

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            User utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        //Ricerca dei ristoranti sul database
        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        RistoranteDAO sessionRistoDAO = DatabaseDAO.getRistoDAO(null);
        ristoranti = sessionRistoDAO.getAll();
        page.addObject("ristoranti", ristoranti);

        DatabaseDAO.closeTransaction();

        return page;
    }

    @GetMapping(path = "/resturantsList", params = {"searchType", "search"})
    public ModelAndView getSearchedResturants(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("searchType") int type,
            @RequestParam("search") String search
    ){
        //Variabili
        ModelAndView page = new ModelAndView("resturantsListPage");
        List<Ristorante> ristoranti;

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            User utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        if (type==1){
            //Ricerca dei ristoranti sul database
            //Accesso al database
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();

            RistoranteDAO sessionRistoDAO = DatabaseDAO.getRistoDAO(null);
            ristoranti = sessionRistoDAO.findByName(search);
            page.addObject("ristoranti", ristoranti);

            DatabaseDAO.closeTransaction();
        } else {
            System.out.println("Unexpected behaviour, returning to homepage");
            return Utility.redirect(page, "/resturantsList");
        }

        return page;
    }
}
