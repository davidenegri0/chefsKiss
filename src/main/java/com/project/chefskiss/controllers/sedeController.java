package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class sedeController {
    @RequestMapping (value = "/sede", params = "id")
    public ModelAndView onSedeViewRequest(
            HttpServletResponse response,
            @RequestParam(name = "id") String id,
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    ){
        ModelAndView page = new ModelAndView("sedePage");
        if(!UserData.isEmpty()){
            User utente = User.decodeUserData(UserData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        // trova le informazioni relative a quella sede
        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        Sede sede = sedeDAO.findByCoordinate(id);
        RistoranteDAO ristoranteDAO = DatabaseDAO.getRistoDAO(null);
        sede.setRistoranteS(ristoranteDAO.findById(sede.getID_Ristorante()));

        // trova l'utente chef della sede
        UserDAO userDAO = DatabaseDAO.getUserDAO(null);
        User chef = userDAO.findBySede(sede);

        // trova lista piatti serviti in sede
        PiattoDAO piattoDAO = DatabaseDAO.getPiattoDAO(null);
        List<Piatto> piatti = new ArrayList<>();
        piatti = piattoDAO.findBySede(sede);

        // trova lista di valutazioni per la sede
        ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
        List<Valutazione> valutazioni = new ArrayList<>();
        valutazioni = valutazioneDAO.findBySede(sede);
        List<User> recensori = new ArrayList<>();
        for ( int i = 0; i < valutazioni.size(); i++){
            valutazioni.get(i).setUtenteV(userDAO.findByCF(valutazioni.get(i).getUtenteV().getCF()));
        }
            //recensori.add();


        System.out.println(sede.getCoordinate());
        System.out.println(chef.getNome()+" "+chef.getCognome());
        System.out.println(piatti.size());
        for (int i = 0; i < piatti.size(); i++) {
            System.out.println(piatti.get(i).getNome());
        }
        for (int i = 0; i < valutazioni.size(); i++){
            System.out.println(valutazioni.get(i).getVoto());
        }

        page.addObject("sede", sede);
        page.addObject("chef", chef);
        page.addObject("piatti", piatti);
        page.addObject("valutazioni", valutazioni);

        return page;
    }
}
