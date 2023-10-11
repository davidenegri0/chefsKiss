package com.project.chefskiss.controllers;

import com.project.chefskiss.Comparators;
import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class resturantsListController {

    @GetMapping(path = "/resturantsList")
    public ModelAndView viewResturantsList(
            @RequestParam(value = "ord", required = false, defaultValue = "0") int ord,
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        //Variabili
        ModelAndView page = new ModelAndView("resturantsListPage");
        List<Ristorante> ristoranti;
        List<Sede> sedi;
        User utente = null;

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");


        //Ricerca dei ristoranti sul database
        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        RistoranteDAO sessionRistoDAO = DatabaseDAO.getRistoDAO(null);
        SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);

        ristoranti = sessionRistoDAO.getAll();
        sedi = sessionSedeDAO.getAll();

        try {
            switch (ord)
            {
                case 1:
                {
                    System.out.println("Ordinamento per nome");
                    ristoranti.sort(Comparators.RistoranteByNome);
                    break;
                }
                case 2:
                {
                    System.out.println("Ordinamento per voto");
                    sedi.sort(Comparators.SedeByVoto);
                    break;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        page.addObject("ristoranti", ristoranti);
        page.addObject("sedi", sedi);
        page.addObject("searched", false);

        DatabaseDAO.closeTransaction();

        return page;
    }

    @GetMapping(path = "/resturantsList", params = {"searchType", "search"})
    public ModelAndView getSearchedResturants(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("searchType") int type,
            @RequestParam("search") String search,
            @RequestParam(value = "ord", required = false, defaultValue = "0") int ord
    ){
        //Variabili
        ModelAndView page = new ModelAndView("resturantsListPage");
        List<Ristorante> ristoranti;
        List<Sede> sedi = null;

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
        } else if (type==2){
            //Ricerca dei ristoranti sul database
            //Accesso al database
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();

            SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);
            sedi = sessionSedeDAO.findByCitta(search);

            RistoranteDAO sessionRistoranteDAO = DatabaseDAO.getRistoDAO(null);
            ristoranti = sessionRistoranteDAO.getAll();

            page.addObject("sedi", sedi);
            page.addObject("ristoranti", ristoranti);

            DatabaseDAO.closeTransaction();
        } else {
            System.out.println("Unexpected behaviour, returning to homepage");
            return Utility.redirect(page, "/resturantsList");
        }
        
        try {
            switch (ord)
            {
                case 1:
                {
                    System.out.println("Ordinamento per nome");
                    ristoranti.sort(Comparators.RistoranteByNome);
                    break;
                }
                case 2:
                {
                    System.out.println("Ordinamento per voto");
                    sedi.sort(Comparators.SedeByVoto);
                    break;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        page.addObject("searched", true);

        return page;
    }
}
