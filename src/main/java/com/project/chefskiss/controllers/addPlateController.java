package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;

@Controller
public class addPlateController {

    //Metodo di visualizzazione della pagina di aggiunta di un piatto
    @GetMapping(path = "/addPlate")
    public ModelAndView getAddPlate(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
    ){
        //Variabili
        ModelAndView page = new ModelAndView("addPlatePage");
        User utente;
        List<Ingrediente> listaIngredienti;

        //Lettura cookie utente
        if(userData.isEmpty()){
            System.out.println("No cookies, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        }
        else
        {
            utente = User.decodeUserData(userData);
            //page.addObject("user", utente);
        }

        //Controllo validità utente
        if (!(utente.isPrivato() || utente.isChef())){
            System.out.println("User is not Privato nor Chef, unexpected behaviour, " +
                    "returning to homepage");
            page.setViewName("index");
            return page;
        }

        //Aggiunta lista totale ingredienti da database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        //Molto brutto, ma necessario, ricerca coordinate
        if (utente.isChef()){
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            utente.setSedeU(sessionUserDAO.findByCF(utente.getCF()).getSedeU());
            System.out.println("Ricerca coordinate sul db completata: "+utente.getSedeU().getCoordinate());
        }

        page.addObject("user", utente);

        IngredienteDAO sessionIngrediente = DatabaseDAO.getIngredienteDAO(null);
        listaIngredienti = sessionIngrediente.getAllIngredients();

        DatabaseDAO.closeTransaction();

        page.addObject("listaIngredienti", listaIngredienti);

        return page;
    }

    @PostMapping(path = "/addPlate", params = {"nomePiatto"})
    public ModelAndView postAddPlate(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("nomePiatto") String nomePiatto,
            @RequestParam(value = "preparazione", required = false) String preparazione,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("ingredienti") List<String> ingredienti,
            @RequestParam("quantita") List<Integer> quantita,
            @RequestParam(value = "sede", required = false, defaultValue = "") String sedeCoord
    ){
        //Variabili
        ModelAndView page = new ModelAndView("index");
        User utente;
        Piatto piatto;
        Ingrediente ingrediente = new Ingrediente();

        //Lettura cookie utente
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

        //Controllo validità utente
        if (!(utente.isPrivato() || utente.isChef())){
            System.out.println("User is not Privato nor Chef, unexpected behaviour, " +
                    "returning to homepage");
            page.setViewName("index");
            return page;
        }

        //Controllo argomenti caricamento ricetta
        /*
        System.out.println(
                "Dati da inviare al database: " + "\n" +
                "CF: " + utente.getCF() + "\n" +
                "Nome piatto: " + nomePiatto + "\n" +
                "Preparazione: " + preparazione + "\n" +
                "Ingredienti: " + ingredienti + "\n" +
                "Quantità: " + quantita
        );
        */

        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        try {
            //Caricamento del nuovo piatto sul database
            PiattoDAO sessionPiattoDAO = DatabaseDAO.getPiattoDAO(null);
            piatto = sessionPiattoDAO.create(nomePiatto, preparazione, utente, new SerialBlob(file.getBytes()));

            //Caricamento nuova lista ingredienti sul database
            ContieneDAO sessionContieneDAO = DatabaseDAO.getContieneDAO(null);
            for (int i = 0; i < ingredienti.size(); i++) {
                ingrediente.setNome(ingredienti.get(i));
                sessionContieneDAO.create(piatto, ingrediente, quantita.get(i));
            }

            System.out.println(sedeCoord);

            //Caricamento piatto servito in sede
            if (!sedeCoord.isEmpty()) {
                Sede sede1 = new Sede();
                sede1.setCoordinate(sedeCoord);
                sessionPiattoDAO.addPiattoinSede(piatto, sede1);
            }
        } catch (Exception e){
            DatabaseDAO.rollbackTransaction();
            DatabaseDAO.closeTransaction();
            throw new RuntimeException(e.getMessage());
        }

        DatabaseDAO.commitTransaction();
        DatabaseDAO.closeTransaction();

        return Utility.redirect(page, "/recipesView");
    }
}
