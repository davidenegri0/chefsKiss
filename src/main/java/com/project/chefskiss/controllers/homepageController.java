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

import java.util.List;

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
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
        List<Piatto> piatti = sessionPiattiDAO.findMostRecent(4);
        DatabaseDAO.closeTransaction();

        //TODO: (EVENTUALE) Si può aggiornare questa feature gestendo le recensioni in base al voto medio
        if (piatti.size() < 4) System.out.println("Strano");

        //Debug dei voti
        /*
        for (int i = 0; i < piatti.size(); i++) {
            System.out.println(piatti.get(i).getNome()+" Voto: "+piatti.get(i).getVotoMedio());
        }
         */

        page.addObject("listaPiatti", piatti);

        return page;
    }
}
