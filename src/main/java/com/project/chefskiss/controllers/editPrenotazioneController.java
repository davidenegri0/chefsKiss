package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;


@Controller
public class editPrenotazioneController {
    @GetMapping(path = "/editPrenotazione", params = {"id", "coordinate"})
    public ModelAndView viewModifyPrenotazione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") Integer id, // id prenotazione
            @RequestParam("coordinate") String coordinate
    ){
        ModelAndView page = new ModelAndView("prenotazionePage");
        User utente;
        boolean isPrenotazioneUp = false;

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
/*
        isPrenotazioneUp = prenotazioneDAO.isPrenotazioneUp(id);

        if (isPrenotazioneUp){ // prenotazione non esistente --> impossibile modifica
            System.out.println(
                    "Non è stata ancora inserita alcuna prenotazione dall'utente " + utente.getCF() +
                            " per il ristorante " + id
            );
            page = Utility.redirect(page, "/prenotazioniList?id="+id);

            page.addObject("errorCode", 1);
            return page;
        }*/

        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        Sede sede = sedeDAO.findByCoordinate(coordinate);

        Prenotazione prenotazione = prenotazioneDAO.findById(id);
        prenotazione.setSedeP(sede);
        prenotazione.setUtenteP(utente);

        //page.addObject("sede", sede);
        page.addObject("prenotazione", prenotazione);
        page.addObject("sede", sede);
        page.addObject("type", 2);

        DatabaseDAO.closeTransaction();

        return page;
    }

    @PostMapping(path = "editPrenotazione", params = {"id", "coordinate", "data", "orario", "n_posti"})
    public ModelAndView postModifyPrenotazione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") Integer id, // id prenotazione
            @RequestParam("coordinate") String coordinate, // coordinate sede
            @RequestParam("data") Date data,
            @RequestParam("orario") String orario_s,
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

        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        Sede sede = sedeDAO.findByCoordinate(coordinate);
        PrenotazioneDAO prenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);
        Prenotazione prenotazione = new Prenotazione();

        if (!prenotazioneDAO.isPrenotazioneUp(utente.getCF(), data, orario)){
            System.out.println(
                    "Prenotazione dell'utente "+utente.getCF()+
                            " per il ristorante "+coordinate+
                            " nell'orario "+orario_s+
                            " in data "+data+
                            " già esistente"
            );
            Integer errorCode = 1;
            page = Utility.redirect(page, "/prenotazioniList?id="+id+"&error="+errorCode);

            page.addObject("errorCode", 4);
            return page;
        }

        prenotazione.setId(id);
        prenotazione.setSedeP(sede);
        prenotazione.setUtenteP(utente);
        prenotazione.setData(data);
        prenotazione.setOrario(orario);
        prenotazione.setN_Posti(n_posti);

        prenotazioneDAO.update(prenotazione);

        DatabaseDAO.commitTransaction();

        page = Utility.redirect(page, "/prenotazioniList?id="+id);

        DatabaseDAO.closeTransaction();

        return page;
    }

}
