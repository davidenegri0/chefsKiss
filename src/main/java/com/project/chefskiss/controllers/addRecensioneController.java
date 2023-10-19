package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class addRecensioneController {
    @GetMapping(path = "/addRecensione", params = {"type", "id"})
    public ModelAndView viewAddRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int typeCode,
            @RequestParam("id") String id
    ){
        //Variabili
        ModelAndView page = new ModelAndView("addRecensionePage");
        User utente;
        boolean isRecensioneUp = false;
        boolean isValutazioneUp = false;
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

        //Accesso al database per la ricerca dei piatti o ristoranti da visualizzare
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        RecensioneDAO sessionRecensioneDAO = DatabaseDAO.getRecensioneDAO(null);
        ValutazioneDAO sessioneValutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
        PrenotazioneDAO sessionePrenotazioneDAO = DatabaseDAO.getPrenotazioneDAO(null);

        // typecode = 1 --> aggiunta recensione piatto
        // typecode = 2 --> aggiunta recensione sede ristorante

        if (typeCode == 1) isRecensioneUp = sessionRecensioneDAO.checkRecensione(utente.getCF(), Integer.parseInt(id)); //String CF, int ID
        else {
            isValutazioneUp = sessioneValutazioneDAO.checkValutazione(utente.getCF(), id);
            isPrenotazioneUp = sessionePrenotazioneDAO.checkPrenotazione(utente.getCF());
        }


        if (typeCode == 1 && isRecensioneUp){ // recensione già inserita --> impossibile aggiunta
            System.out.println(
                    "Recensione dell'utente "+utente.getCF()+
                    " per il piatto "+id+" già presente"
            );
            Integer errorCode = 1;
            page = Utility.redirect(page, "/plate?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */

            return page;
        }

        if (typeCode == 2 && isValutazioneUp){ // valutazione già inserita --> impossibile aggiunta
            System.out.println(
                    "Recensione dell'utente "+utente.getCF()+
                            " per il ristorante "+id+" già presente"
            );
            Integer errorCode = 1;
            page = Utility.redirect(page, "/sede?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 4);
            return page;
        }
        if (typeCode == 2 && !isPrenotazioneUp){ // valutazione già inserita --> impossibile aggiunta
            System.out.println(
                    "Recensione dell'utente "+utente.getCF()+
                            " per il ristorante "+id+" già presente"
            );
            Integer errorCode = 4;
            page = Utility.redirect(page, "/sede?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 4);
            return page;
        }


        switch (typeCode){
            case 1: //typeCode = 1 --> recensione di un piatto
            {
                int plateID = Integer.parseInt(id);

                PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto plate = sessionPiattiDAO.findByIDPiatto(plateID);

                User utente_p = plate.getUtenteP();

                UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
                utente_p = sessionUserDAO.findByCF(utente_p.getCF());

                plate.setUtenteP(utente_p);

                page.addObject("piatto", plate);
                page.addObject("typecode", typeCode);
                //page.addObject("isRistorante", false);

                break;
            }
            case 2: //typeCode = 2 --> recensione di una sede di un ristorante
            {
                SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
                Sede sede = sedeDAO.findByCoordinate(id);
                /*
                Ristorante ristorante = sede.getRistoranteS(); // prende oggetto ristorante di sede

                RistoranteDAO ristoranteDAO = DatabaseDAO.getRistoDAO(null);
                ristorante = ristoranteDAO.findById(ristorante.getID_Ristorante()); // cerca tutti i dati del ristorante della sede dall'id ristorante

                sede.setRistoranteS(ristorante); // setta in sede tutti i dati del ristorante
                */

                page.addObject("sede", sede);
                page.addObject("typecode", typeCode);
                //page.addObject("isRistorante", true);

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

                try{
                    sessionRecensioneDAO.create(utente, piatto, voto, commento);

                    DatabaseDAO.commitTransaction();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    DatabaseDAO.rollbackTransaction();
                }

                //System.out.println("Per piatto con ID: "+ID+" Votazione: "+voto+" e commento: "+commento);

                page = Utility.redirect(page, "/plate?id="+plateID);

                break;
            }
            case 2: //typeCode = 2 --> recensione di un ristorante
            {
                //Cercare i dati del ristorante nel db/cookie, e inviarli alla pageù

                ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
                SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
                Sede sede = sedeDAO.findByCoordinate(ID);

                try {
                    valutazioneDAO.create(utente, sede, voto);

                    //page.addObject("isRistorante", true);
                    DatabaseDAO.commitTransaction();
                } catch (RuntimeException e){
                    e.printStackTrace();
                    DatabaseDAO.rollbackTransaction();
                }

                page = Utility.redirect(page, "/sede?id="+ID);

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
