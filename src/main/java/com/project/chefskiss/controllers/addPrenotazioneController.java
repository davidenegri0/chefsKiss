package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class addPrenotazioneController {
    @GetMapping(path = "/addPrenotazione", params = {"coordinate"})
    public ModelAndView viewAddPrenotazione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("coordinate") String coordinate
    ){
        ModelAndView page = new ModelAndView("prenotazionePage");
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

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);

        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        Sede sede = sedeDAO.findByCoordinate(coordinate);

        page.addObject("sede", sede);
        page.addObject("type", 1);

        DatabaseDAO.closeTransaction();

        return page;
    }

    @PostMapping(path = "/addPrenotazione", params = {"coordinate", "data", "orario", "n_posti", "CF" })
    public ModelAndView postPrenotazione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("coordinate") String coordinate, // coordinate sede
            @RequestParam("CF") String CF,
            @RequestParam("data")Date data,
            @RequestParam("orario")String orario_s,
            @RequestParam("n_posti") Integer n_posti
            ){
        ModelAndView page = new ModelAndView("index");
        User utente;
        Time orario = null;
        try {
            Date dataCorrente = new Date(System.currentTimeMillis());
            // controllo sulla data
            //if (data.before(dataCorrente)) throw new RuntimeException("Data scelta preeccdente alla data attuale");

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            java.util.Date parsedDate = sdf.parse(orario_s);
            long ms = parsedDate.getTime();
            orario = new Time(ms);
        } catch (Exception e){
            e.printStackTrace();
        }
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

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);
        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);;
        Sede sede = sedeDAO.findByCoordinate(coordinate);
        if (prenotazioneDAO.verifica_posti_disponibili(coordinate, orario, data) < n_posti){
            System.out.println(
                    "Prenotazione dell'utente "+utente.getCF()+
                            " per il ristorante "+coordinate+" non eseguita"
            );
            Integer errorCode = 5;
            page = Utility.redirect(page, "/sede?id="+coordinate+"&error="+errorCode);

            page.addObject("errorCode", 4);
            return page;
        }
        if (!prenotazioneDAO.isPrenotazioneUp(utente.getCF(), data, orario)){
            System.out.println(
                    "Prenotazione dell'utente "+utente.getCF()+
                            " per il ristorante "+coordinate+
                            " nell'orario "+orario_s+
                            " in data "+data+
                            " già esistente"
            );
            Integer errorCode = 6;
            page = Utility.redirect(page, "/sede?id="+coordinate+"&error="+errorCode);

            page.addObject("errorCode", 4);
            return page;
        }
        try{
            prenotazioneDAO.create(utente, sede, data, orario, n_posti);

            DatabaseDAO.commitTransaction();
        } catch (RuntimeException e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
        }


        page = Utility.redirect(page, "/sede?id="+coordinate);

        DatabaseDAO.closeTransaction();

        return page;
    }
}