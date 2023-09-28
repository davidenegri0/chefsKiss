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
import java.util.List;

//TODO: Andare a implementare il salvataggio di piatti sui cookie

@Controller
public class recipesListController {
    @GetMapping(path = "/recipesView")
    public ModelAndView viewRecipesMenu(
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
        List<Piatto> piatti = sessionPiattiDAO.findMostRecent(10);
        DatabaseDAO.closeTransaction();

        //Invio lista dei piatti alla pagina
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
        @RequestParam(value = "allergene0", required = false) String allergene0,
        @RequestParam(value = "allergene1", required = false) String allergene1,
        @RequestParam(value = "allergene2", required = false) String allergene2,
        @RequestParam(value = "allergene3", required = false) String allergene3,
        @RequestParam(value = "allergene4", required = false) String allergene4
    ){
        ModelAndView page = new ModelAndView("recipesListPage");
        List<Piatto> piatti;

        //Gestione allergeni
        List<String> allergeni = new ArrayList<>();
        if (allergene0!=null) allergeni.add(allergene0);
        if (allergene1!=null) allergeni.add(allergene1);
        if (allergene2!=null) allergeni.add(allergene2);
        if (allergene3!=null) allergeni.add(allergene3);
        if (allergene4!=null) allergeni.add(allergene4);

        //Lettura dei cookie dell'utente
        if(!userData.isEmpty()){
            User utente = User.decodeUserData(userData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        //System.out.println("Tipo di ricerca: "+type+"; Nome ricetta: "+search);

        //Accesso al database per la ricerca dei piatti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);

        //Controllo allergeni
        for (int i = 0; i < allergeni.size(); i++) {
            System.out.println("Allergene: "+allergeni.get(i));
        }

        //TODO: Gestire gli allergeni

        //Se type == 1 --> Ricerca per nome, se type == 2 --> Ricerca per ingrediente
        if (type==1)
        {
            piatti = sessionPiattiDAO.findByName(search.toLowerCase());
        } else if (type==2) {
            Ingrediente i = new Ingrediente();
            i.setNome(search);
            piatti = sessionPiattiDAO.findByIngediente(i);
        } else {
            piatti = sessionPiattiDAO.findMostRecent(10);
        }

        DatabaseDAO.closeTransaction();
        page.addObject("listaPiatti", piatti);

        page.addObject("searched", true);

        return page;
    }
}
