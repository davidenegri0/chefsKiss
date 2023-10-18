package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class deleteRecensioneController {
    @GetMapping (path = "/deleteRecensione")
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
        else isValutazioneUp = sessioneValutazioneDAO.checkValutazione(utente.getCF(), id);


        if (type == 3 && !isRecensioneUp){ // recensione non esistente --> impossibile cancellazione
            System.out.println(
                    "Non è stata ancora inserita alcuna recensione dall'utente " + utente.getCF() +
                            " per il piatto " + id
            );
            Integer errorCode = 3;
            page = Utility.redirect(page, "/plate?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 1);
            return page;
        }

        if (type == 4 && !isValutazioneUp){ // valutazione non esistente --> impossibile cancellazione
            System.out.println(
                    "Non è stata ancora inserita alcuna valutazione dall'utente " + utente.getCF() +
                            " per il ristorante " + id
            );
            Integer errorCode = 3;
            page = Utility.redirect(page, "/sede?id="+id+"&error="+errorCode);
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
                Recensione recensione = recensioneDAO.findByPiatto_Utente(Integer.parseInt(id), utente.getCF());

                try{
                    recensioneDAO.delete(recensione);

                    DatabaseDAO.commitTransaction();
                } catch (RuntimeException e){
                    e.printStackTrace();
                    DatabaseDAO.rollbackTransaction();
                    DatabaseDAO.closeTransaction();
                }

                page = Utility.redirect(page, "/plate?id="+Integer.parseInt(id));

                break;
            }
            case 4: //typeCode = 2 --> valutazione di una sede di un ristorante
            {
                SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
                Sede sede = sedeDAO.findByCoordinate(id);

                ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
                Valutazione valutazione = valutazioneDAO.findByCF_Coordinate(utente.getCF(), sede.getCoordinate());

                try {
                    valutazioneDAO.delete(valutazione);

                    DatabaseDAO.commitTransaction();
                } catch (RuntimeException e){
                    e.printStackTrace();
                    DatabaseDAO.rollbackTransaction();
                }

                page = Utility.redirect(page, "/sede?id="+id);

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

