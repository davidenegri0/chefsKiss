package com.project.chefskiss.controllers;

import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.ContieneDAO;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.IngredienteDAO;
import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Contiene;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class editPlateController {
    @GetMapping(path = "/editPlate", params = "id")
    public ModelAndView viewAndEditPlate(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") int id
    ){
        //Variabili
        ModelAndView page = new ModelAndView("editPlatePage");
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

        //Ricerca del piatto sul database
        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        //Ricerca del piatto per ID
        PiattoDAO sessionPiattoDAO = DatabaseDAO.getPiattoDAO(null);
        Piatto piatto = sessionPiattoDAO.findByIDPiatto(id);
        page.addObject("piatto", piatto);

        if (!(piatto.getUtenteP().getCF().equals(utente.getCF()))){
            System.out.println("User not correspondig to plate owner, unexpected behaviour, returning to homepage");
            page.setViewName("index");
            return page;
        }

        //Ricerca degli ingredienti del piatto
        ContieneDAO sessionConteutoDAO = DatabaseDAO.getContieneDAO(null);
        List<Contiene> contenuto = sessionConteutoDAO.findByPiatto(piatto);

        page.addObject("contenutoPiatto", contenuto);

        IngredienteDAO sessionIngredienteDAO = DatabaseDAO.getIngredienteDAO(null);
        List<Ingrediente> ingredienti = sessionIngredienteDAO.getAllIngredients();

        page.addObject("listaIngredienti", ingredienti);

        //Chiusura
        DatabaseDAO.closeTransaction();

        //... ?

        return page;
    }

    @PostMapping(path = "/editPlate", params = {"id", "nomePiatto"})
    public ModelAndView postAddPlate(
            @CookieValue(value = "loggedUser", defaultValue = "") String userData,
            @RequestParam("id") int id,
            @RequestParam("nomePiatto") String nomePiatto,
            @RequestParam(value = "preparazione", required = false) String preparazione,
            @RequestParam("ingredienti") List<String> ingredienti,
            @RequestParam("quantita") List<Integer> quantita
    ){
        //Variabili
        ModelAndView page = new ModelAndView("index");
        User utente;
        Piatto piatto;
        Ingrediente ingrediente = new Ingrediente();
        Contiene contenuto = null;

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

        System.out.println(
                "Dati da inviare al database: " + "\n" +
                "CF: " + utente.getCF() + "\n" +
                "Nome piatto: " + nomePiatto + "\n" +
                "Preparazione: " + preparazione + "\n" +
                "Ingredienti: " + ingredienti + "\n" +
                "Quantità: " + quantita + "\n" +
                "Per il piatto: " + id
        );


        //Accesso al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        //Caricamento delle modifiche al piatto sul database
        PiattoDAO sessionPiattoDAO = DatabaseDAO.getPiattoDAO(null);
        piatto = sessionPiattoDAO.findByIDPiatto(id);
        piatto.setNome(nomePiatto);
        piatto.setPreparazione(preparazione);
        sessionPiattoDAO.update(piatto);
        DatabaseDAO.commitTransaction();

        //Caricamento nuova lista ingredienti sul database
        ContieneDAO sessionContieneDAO = DatabaseDAO.getContieneDAO(null);
        for (int i = 0; i < ingredienti.size(); i++) {

            ingrediente.setNome(ingredienti.get(i));

            contenuto = sessionContieneDAO.findByPiatto_Ingrediente(piatto, ingrediente);

            if (contenuto.getPiattoC()!=null){
                contenuto.setPiattoC(piatto);

                contenuto.setIngredienteC(ingrediente);

                contenuto.setQuantita(quantita.get(i));

                sessionContieneDAO.update(contenuto);
            } else {
                sessionContieneDAO.create(piatto, ingrediente, quantita.get(i));
            }

            DatabaseDAO.commitTransaction();
        }

        DatabaseDAO.closeTransaction();

        //Non so se serva fare altro

        return Utility.redirect(page, "/plate?id="+id);
    }
}
