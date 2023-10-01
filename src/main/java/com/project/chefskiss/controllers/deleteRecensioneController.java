package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Recensione;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deleteRecensioneController {
    @RequestMapping (path = "/deleteRecensione", method = RequestMethod.GET)
    public ModelAndView DeleteRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int type,
            @RequestParam("id") String id
    ){
        ModelAndView page = new ModelAndView("index");
        User utente;
        boolean isRecensioneUp = false;
        boolean isValutazioneUp = false;

        System.out.println(type);
        System.out.println(id);

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

        RecensioneDAO sessionRecensioneDAO = DatabaseDAO.getRecensioneDAO(null);
        ValutazioneDAO sessioneValutazioneDAO = DatabaseDAO.getValutazioneDAO(null);

        // typecode = 3 --> modifica recensione piatto
        // typecode = 4 --> modifica recensione sede ristorante

        if (type == 3) isRecensioneUp = sessionRecensioneDAO.checkRecensione(utente.getCF(), Integer.parseInt(id)); //String CF, int ID
        else isValutazioneUp = sessioneValutazioneDAO.checkValutazione(utente.getCF(), id); //TODO: Implementare controllo recensione ristorante


        if (type == 3 && !isRecensioneUp){ // recensione non esistente --> impossibile cancellazione
            System.out.println(
                    "Non è stata ancora inserita alcuna recensione dall'utente " + utente.getCF() +
                            " per il piatto/ristorante " + id
            );
            page = Utility.redirect(page, "/plate?id="+id);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 1);
            return page;
        }


        switch (type){
            case 3: //typeCode = 1 --> recensione di un piatto
            {
                PiattoDAO piattoDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto piatto = piattoDAO.findByIDPiatto(Integer.parseInt(id));

                RecensioneDAO recensioneDAO = DatabaseDAO.getRecensioneDAO(null);
                Recensione recensione = new Recensione();
                recensione = recensioneDAO.findByPiatto_Utente(Integer.parseInt(id), utente.getCF());

                recensioneDAO.delete(recensione);

                DatabaseDAO.commitTransaction();

                page = Utility.redirect(page, "/plate?id="+Integer.parseInt(id));

                break;
            }
            case 4: //typeCode = 2 --> recensione di un ristorante
            {
                //TODO: Implementare il setting per la recensione del ristorante
                //Cercare i dati del ristorante nel db/cookie, e inviarli alla pageù


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

