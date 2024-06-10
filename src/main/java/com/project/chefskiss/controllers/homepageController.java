package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
public class homepageController {
    @RequestMapping(value = "/homepage")
    public ModelAndView homepageLoader(
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    )
    {
        ModelAndView page = new ModelAndView("homepagePage");
        User utente;

        if(!UserData.isEmpty()){
            utente = User.decodeUserData(UserData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
        List<Piatto> piatti = sessionPiattiDAO.findMostRecent();
        DatabaseDAO.closeTransaction();

        if (piatti.size() < 4) System.out.println("Strano");

        Collections.shuffle(piatti); //Perchè si
        page.addObject("listaPiatti", piatti.subList(0,4));

        return page;
    }
}
