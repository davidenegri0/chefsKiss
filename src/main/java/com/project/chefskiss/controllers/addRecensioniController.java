package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.dataAccessObjects.RecensioneDAO;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.project.chefskiss.Utility.*;

@Controller
public class addRecensioniController {
    @GetMapping(path = "/addRecensione", params = {"type", "id"})
    public ModelAndView viewAddRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int typeCode,
            @RequestParam("id") String id
    ){
        //Variabili
        ModelAndView page = new ModelAndView("addRecensionePage");
        User utente;
        boolean isRecensioneUp;

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

        //Accesso al database per la ricerca dei piatti o ristoranti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        RecensioneDAO sessionRecensioneDAO = DatabaseDAO.getRecensioneDAO(null);
        if (typeCode==1) isRecensioneUp = sessionRecensioneDAO.checkRecensione(utente.getCF(), Integer.parseInt(id)); //String CF, int ID
        else isRecensioneUp = true; //TODO: Implementare controllo recensione ristorante

        if (isRecensioneUp){
            System.out.println(
                    "Recensione dell'utente "+utente.getCF()+
                    " per il piatto/ristorante "+id+" già presente"
            );
            page = Utility.redirect(page, "/plate?id="+id);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 1);
            return page;
        }


        switch (typeCode){
            case 1: //typeCode = 1 --> recensione di un piatto
            {
                int plateID = Integer.parseInt(id);

                PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto plate = sessionPiattiDAO.findByIDPiatto(plateID);

                utente = plate.getUtenteP();

                UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
                utente = sessionUserDAO.findByCF(utente.getCF());

                plate.setUtenteP(utente);

                page.addObject("piatto", plate);

                page.addObject("isRistorante", false);

                break;
            }
            case 2: //typeCode = 2 --> recensione di un ristorante
            {
                //TODO: Implementare il setting per il la recensione del ristorante
                //Cercare i dati del ristorante nel db/cookie, e inviarli alla page

                page.addObject("isRistorante", true);

                break;
            }
            default:    //typeCode != 1 o 2 --> che minchia significa?
            {
                System.out.println("typeCode wrong, unexpected behaviour, returning to homepage");
                page.setViewName("index");
                break;
            }
        }

        DatabaseDAO.closeTransaction();

        return page;
    }

    @PostMapping(path = "/addRecensione", params = {"type", "ID", "voto"})
    public ModelAndView postRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int type,
            @RequestParam("ID") String ID,
            @RequestParam("voto") int voto,
            @RequestParam(value = "commento", required = false, defaultValue = "")
            String commento
    ){
        ModelAndView page = new ModelAndView("index");
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

        //Accesso al database per la ricerca dei piatti o ristoranti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        switch (type){
            case 1: //typeCode = 1 --> recensione di un piatto
            {
                int plateID = Integer.parseInt(ID);

                RecensioneDAO sessionRecensioneDAO = DatabaseDAO.getRecensioneDAO(null);
                PiattoDAO sessionPiattoDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto piatto = sessionPiattoDAO.findByIDPiatto(plateID);
                sessionRecensioneDAO.create(utente, piatto, voto, commento);

                DatabaseDAO.commitTransaction();

                //System.out.println("Per piatto con ID: "+ID+" Votazione: "+voto+" e commento: "+commento);

                page = Utility.redirect(page, "/plate?id="+plateID);

                break;
            }
            case 2: //typeCode = 2 --> recensione di un ristorante
            {
                //TODO: Implementare il setting per la recensione del ristorante
                //Cercare i dati del ristorante nel db/cookie, e inviarli alla page

                page.addObject("isRistorante", true);

                break;
            }
            default:    //typeCode != 1 o 2 --> che minchia significa?
            {
                System.out.println("typeCode wrong, unexpected behaviour, returning to homepage");
                page.setViewName("index");
                break;
            }
        }

        DatabaseDAO.closeTransaction();

        return page;
    }
}
