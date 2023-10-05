package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
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
        List<User> chefs = userDAO.findBySede(sede);

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


       /* System.out.println(sede.getCoordinate());
        System.out.println(chef.getNome()+" "+chef.getCognome());
        System.out.println(piatti.size());*/
        for (int i = 0; i < piatti.size(); i++) {
            System.out.println(piatti.get(i).getNome());
        }
        for (int i = 0; i < valutazioni.size(); i++){
            System.out.println(valutazioni.get(i).getVoto());
        }

        page.addObject("sede", sede);
        page.addObject("chefs", chefs);
        page.addObject("piatti", piatti);
        page.addObject("valutazioni", valutazioni);

        return page;
    }

    @GetMapping(path = "/deleteChef", params = {"CF", "ID"})
    public ModelAndView onDeleteChefRequest(
            @RequestParam("CF") String CF,
            @RequestParam("ID") String ID,
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente;

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

        //Connessione al db per rimozione chef da sede
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try{
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            User chef = sessionUserDAO.findByCF(CF);
            chef.setSedeU(null);

            sessionUserDAO.update(chef);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        return Utility.redirect(page, "/sede?id="+ID);
    }

    @GetMapping(path = "/addChef", params = "ID")
    public ModelAndView onAddChefRequest(
            @RequestParam("ID") String ID,
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    )
    {
        //Variabili
        ModelAndView page = new ModelAndView("addChefPage");
        User utente;
        List<User> chefs;
        page.addObject("CoordSede", ID);

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

        //Connesisone al DB
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);

        chefs = sessionUserDAO.getAllFreeChefs();

        DatabaseDAO.closeTransaction();

        page.addObject("chefs", chefs);

        return page;
    }

    @GetMapping(path = "/addChef", params = {"Coord", "CF"})
    public ModelAndView onAddChefRequest(
            @RequestParam("Coord") String Coord,
            @RequestParam("CF") String CF,
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    )
    {
        //Variabili
        ModelAndView page = new ModelAndView();
        User utente;

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

        //Connesisone al DB
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try{
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            User chef = sessionUserDAO.findByCF(CF);

            Sede sede = new Sede();
            sede.setCoordinate(Coord);
            chef.setSedeU(sede);

            sessionUserDAO.update(chef);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }

        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/sede?id="+Coord);
    }
}
