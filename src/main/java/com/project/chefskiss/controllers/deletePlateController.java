package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deletePlateController {
    @GetMapping(path = "/deletePlate", params = "id")
    public ModelAndView onDeletePlateRequest(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") int id
    ){
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente = null;
        Piatto piatto;

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

        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        //Download del piatto dal database
        PiattoDAO sessionePiattoDAO = DatabaseDAO.getPiattoDAO(null);
        piatto = sessionePiattoDAO.findByIDPiatto(id);

        if (!(piatto.getUtenteP().getCF().equals(utente.getCF()))){
            System.out.println("L'untente non ha postato questa ricetta, unexpected behaviour");
            page.setViewName("index");
            return page;
        }

        try {
            //Cancellazione piatto da database
            sessionePiattoDAO.delete(piatto);
            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/recipesView");
    }
}
