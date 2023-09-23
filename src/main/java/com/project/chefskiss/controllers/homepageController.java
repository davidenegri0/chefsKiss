package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class homepageController {
    @RequestMapping(value = "/homepage")
    public ModelAndView homepageLoader(
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    )
    {
        ModelAndView page = new ModelAndView("homepage");

        if(!UserData.isEmpty()){
            User utente = User.decodeUserData(UserData);
            page.addObject("user", utente.getNome()+" "+utente.getCognome());
        }
        else
        {
            System.out.println("No cookies :C");
        }

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
        Piatto[] piatti = sessionPiattiDAO.find4MostRecent();
        DatabaseDAO.closeTransaction();

        //TODO: Si può aggiornare questa feature gestendo le recensioni in base al voto medio
        //e caricare le ricette sui cookie
        if (piatti.length < 4) System.out.println("Strano");
        page.addObject("ricetta1", piatti[0].getNome());
        page.addObject("ricetta2", piatti[1].getNome());
        page.addObject("ricetta3", piatti[2].getNome());
        page.addObject("ricetta4", piatti[3].getNome());

        return page;
    }
}
