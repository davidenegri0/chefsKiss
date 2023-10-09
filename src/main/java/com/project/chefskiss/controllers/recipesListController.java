package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class recipesListController {
    @GetMapping(path = "/recipesView")
    public ModelAndView viewRecipesList(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        ModelAndView page = new ModelAndView("recipesListPage");

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            User utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        //Accesso al database per la ricerca dei piatti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
        List<Piatto> piatti = sessionPiattiDAO.findMostRecent();
        DatabaseDAO.closeTransaction();

        //Invio lista dei piatti alla pagina
        //Collections.shuffle(piatti); //Perchè si
        if (piatti.size()>10) piatti = piatti.subList(0,10);
        page.addObject("listaPiatti", piatti);

        page.addObject("searched", false);

        //...

        return page;
    }

    @GetMapping(path = "/recipesView", params = {"search","searchType"})
    public ModelAndView searchRecipes(
        @CookieValue(value = "loggedUser", defaultValue = "") String userData,
        @RequestParam("search") String search,
        @RequestParam("searchType") int type,
        @RequestParam(value = "allergeni", required = false) List<String> allergeni
    ){
        ModelAndView page = new ModelAndView("recipesListPage");
        List<Piatto> piatti;

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            User utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        //System.out.println("Tipo di ricerca: "+type+"; Nome ricetta: "+search);
        if (search.isBlank()) type = 0;

        //Accesso al database per la ricerca dei piatti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);

        /*
        Se type == 1 --> Ricerca per nome,
        se type == 2 --> Ricerca per ingrediente
        */
        switch (type)
        {
            case 1:
            {
                if (allergeni==null) piatti = sessionPiattiDAO.findByName(search.toLowerCase());
                else piatti = sessionPiattiDAO.findByName(search.toLowerCase(), allergeni);
                break;
            }
            case 2:
            {
                Ingrediente i = new Ingrediente();
                i.setNome(search);

                if (allergeni==null) piatti = sessionPiattiDAO.findByIngediente(i);
                else piatti=sessionPiattiDAO.findByIngediente(i, allergeni);
                break;
            }
            default:
            {
                piatti = sessionPiattiDAO.findMostRecent();
                break;
            }
        }

        DatabaseDAO.closeTransaction();

        if (piatti.size()>10) piatti = piatti.subList(0,10);
        page.addObject("listaPiatti", piatti);

        page.addObject("searched", true);

        return page;
    }
}
