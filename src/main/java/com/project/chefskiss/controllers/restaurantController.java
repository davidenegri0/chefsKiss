package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class restaurantController {
    @RequestMapping(value = "/restaurant", params = "id")
    public ModelAndView onRestaurantViewRequest (
            HttpServletResponse response,
            @RequestParam(name = "id", defaultValue = "", required = true) Integer id_ristorante,
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    ){
        ModelAndView page = new ModelAndView("restaurantPage");
        if(!UserData.isEmpty()){
            User utente = User.decodeUserData(UserData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        // cerco nel database il ristorante richiesto
        RistoranteDAO ristoranteDAO = DatabaseDAO.getRistoDAO(null);
        Ristorante ristorante = new Ristorante();
        ristorante = ristoranteDAO.findById(id_ristorante);
        System.out.println(ristorante);

        // cerco nel database le sedi associate a quel ristorante
        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        List<Sede> sedi = new ArrayList<>();
        sedi = sedeDAO.findByRistorante(ristorante);

        // cerco nel database il ristoratore del ristorante
        UserDAO userDAO = DatabaseDAO.getUserDAO(null);
        User ristoratore = new User();
        String CF_ristoratore = ristorante.getUtenteRi().getCF();
        ristoratore = userDAO.findByCF(CF_ristoratore);


        DatabaseDAO.commitTransaction();

        page.addObject("ristorante", ristorante);
        page.addObject("sedi", sedi);
        page.addObject("ristoratore", ristoratore);

        return page;
    }
}
