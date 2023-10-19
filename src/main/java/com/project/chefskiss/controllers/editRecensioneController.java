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
public class editRecensioneController {
    @GetMapping(path = "/modifyRecensione", params = {"type", "id"})
    public ModelAndView viewModifyRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int typeCode,
            @RequestParam("id") String id
    ){
        //Variabili
        ModelAndView page = new ModelAndView("addRecensionePage");
        User utente;
        boolean isRecensioneUp = false;
        boolean isValutazioneUp = false;

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

        // typecode = 3 --> modifica recensione piatto
        // typecode = 4 --> modifica recensione sede ristorante

        if (typeCode == 3) isRecensioneUp = sessionRecensioneDAO.checkRecensione(utente.getCF(), Integer.parseInt(id)); //String CF, int ID
        else isValutazioneUp = sessioneValutazioneDAO.checkValutazione(utente.getCF(), id);  //String CF, String ID

        if (typeCode == 3 && !isRecensioneUp){ // recensione non esistente --> impossibile modifica
            System.out.println(
                    "Non è stata ancora inserita alcuna recensione dall'utente " + utente.getCF() +
                            " per il piatto " + id
            );
            Integer errorCode = 2;
            page = Utility.redirect(page, "/plate?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 1);
            return page;
        }

        if (typeCode == 4 && !isValutazioneUp){ // valutazione non esistente --> impossibile modifica
            System.out.println(
                    "Non è stata ancora inserita alcuna recensione dall'utente " + utente.getCF() +
                            " per il ristorante " + id
            );
            Integer errorCode = 2;
            page = Utility.redirect(page, "/sede?id="+id+"&error="+errorCode);
            /*
            page.setViewName("redirect_to");
            page.addObject("url", "/plate?id="+id);
            */
            page.addObject("errorCode", 1);
            return page;
        }


        switch (typeCode){
            case 3: // modifica di una recensione di un piatto
            {
                int plateID = Integer.parseInt(id);

                PiattoDAO sessionPiattiDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto plate = sessionPiattiDAO.findByIDPiatto(plateID); // cerca il piatto in questione

                User utente_p = plate.getUtenteP(); // cerca il cf dell'utente associato al piatto

                UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
                utente_p = sessionUserDAO.findByCF(utente_p.getCF()); // cerfca tutte le informazioni dell'utente associato al piatto

                plate.setUtenteP(utente_p); // inserisce in piatto tutte le informazioni dell'utente associato


                RecensioneDAO recensioneDAO = DatabaseDAO.getRecensioneDAO(null);
                Recensione recensione = recensioneDAO.findByPiatto_Utente(plateID, utente.getCF()); // cerca la recensione da modificare in base a utente loggato e piatto
                recensione.setPiattoR(plate); // inserisce in recensione il piatto completo anche con le informazioni di utente
                //recensione.setUtenteR(utente);


                page.addObject("piatto", plate); // contiene tutte informazioni del piatto comprese quelle dell'utente che l'ha caricato
                page.addObject("recensione", recensione);
                page.addObject("typecode", typeCode);
                page.addObject("isRistorante", false);

                break;
            }
            case 4: // modifica di una valutazione di un ristorante
            {
                SedeDAO sessionSedeDAO = DatabaseDAO.getSedeDAO(null);
                Sede sede = sessionSedeDAO.findByCoordinate(id); // cerca le informazioni per la sede

                ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);
                Valutazione valutazione = valutazioneDAO.findByCF_Coordinate(utente.getCF(), sede.getCoordinate());

                valutazione.setSedeV(sede); // inserisce in valutazione la sede completa anche con le informazioni di utente
                valutazione.setUtenteV(utente); // inserisce in valutazione l'utente loggato con tutte le sue informazioni

                page.addObject("sede", sede); // contiene tutte informazioni della sede
                page.addObject("valutazione", valutazione);
                page.addObject("typecode", typeCode);
                //page.addObject("isRistorante", false);

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

    @PostMapping(path = "/modifyRecensione", params = {"type", "ID", "voto"})
    public ModelAndView postModifyRecensione(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("type") int type,
            @RequestParam("ID") String id,
            @RequestParam("voto") Integer voto,
            @RequestParam(value = "commento", defaultValue = "", required = false) String commento
    ){
        ModelAndView page = new ModelAndView("index");
        User utente;

        System.out.println(id + " " + voto + " " + commento + " " + type);

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
            case 3: //typeCode = 1 --> recensione di un piatto
            {
                PiattoDAO piattoDAO = DatabaseDAO.getPiattoDAO(null);
                Piatto piatto = piattoDAO.findByIDPiatto(Integer.parseInt(id));

                RecensioneDAO recensioneDAO = DatabaseDAO.getRecensioneDAO(null);

                try {
                    Recensione recensione = new Recensione();
                    recensione.setVoto(voto);
                    recensione.setCommento(commento);
                    recensione.setUtenteR(utente);
                    recensione.setPiattoR(piatto);
                    System.out.println(recensione.getPiattoR().getNome() + " " + recensione.getVoto() + " " + recensione.getCommento() + " " + recensione.getUtenteR().getNome());

                    recensioneDAO.update(recensione);

                    DatabaseDAO.commitTransaction();
                } catch (RuntimeException e){
                    e.printStackTrace();
                    DatabaseDAO.rollbackTransaction();
                }

                page = Utility.redirect(page, "/plate?id="+Integer.parseInt(id));

                break;
            }
            case 4: //typeCode = 2 --> recensione di un ristorante
            {
                SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
                Sede sede = sedeDAO.findByCoordinate(id);

                ValutazioneDAO valutazioneDAO = DatabaseDAO.getValutazioneDAO(null);

                try {
                    Valutazione valutazione = new Valutazione();
                    valutazione.setUtenteV(utente);
                    valutazione.setSedeV(sede);
                    valutazione.setVoto(voto);

                    valutazioneDAO.update(valutazione);

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
