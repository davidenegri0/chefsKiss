package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.modelObjects.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class plateController {
    @RequestMapping(value = "/plate")
    public ModelAndView onPlateViewRequest(
            HttpServletResponse response,
            @RequestParam(name = "id", defaultValue = "", required = false) Integer id_piatto,
            @CookieValue(value = "loggedUser", defaultValue = "") String UserData
    ){

        ModelAndView page = new ModelAndView("platePage");
        if(!UserData.isEmpty()){
            User utente = User.decodeUserData(UserData);
            page.addObject("user", utente);
        }
        else System.out.println("No cookies :C");

        // cerco nel database il piatto con l'id richiesto
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();

        Piatto piatto = new Piatto();

        PiattoDAO piatto_richiesto_DAO = DatabaseDAO.getPiattoDAO(null);

        piatto = piatto_richiesto_DAO.findByIDPiatto(id_piatto);// in piatto ho il piatto con l'id passato per parametro


        //System.out.println("Nome:" + piatto.getNome());
        //System.out.println("ID: " + piatto.getId());

        // cerco gli ingredienti del piatto richiesto
        ContieneDAO contieneDAO = DatabaseDAO.getContieneDAO(null);

        List<Contiene> contiene = new ArrayList<>();
        contiene = contieneDAO.findByPiatto(piatto);
        System.out.println("Ingredienti trovati: " + contiene.size());

        for(int i = 0; i < contiene.size(); i++){
            System.out.println(contiene.get(i).getIngredienteC().getNome());
        }

        System.out.println("CF_Utente: " + piatto.getUtenteP().getCF());
        String CF_utente = piatto.getUtenteP().getCF();

        // cerco l'utente che ha pubblicato il piatto
        UserDAO userDAO = DatabaseDAO.getUserDAO(null);
        User utente_post;
        utente_post = userDAO.findByCF(CF_utente);
        System.out.println("Utente: " + utente_post.getNome());

        if (piatto.getPreparazione() == null) piatto.setPreparazione("Preparazione tenuta gelosamente segreta da " + utente_post.getNome() + " " + utente_post.getCognome());

        // cerco le sedi in cui è servito il piatto
        SedeDAO sedeDAO = DatabaseDAO.getSedeDAO(null);
        List<Sede> coordinate = new ArrayList<>();
        coordinate = sedeDAO.findByPiatto(piatto);
        List<Sede> sedi = new ArrayList<>();

        for (int i = 0; i < coordinate.size(); i++){
            sedi.add(sedeDAO.findByCoordinate(coordinate.get(i).getCoordinate()));
            System.out.println("Ristorante della sede: " + sedi.get(i).getID_Ristorante());
        }

        // cerco il ristorante delle sedi trovate
        RistoranteDAO ristoranteDAO = DatabaseDAO.getRistoDAO(null);
        List<Ristorante> ristoranti = new ArrayList<>();
        for (int i = 0; i < sedi.size(); i++){
            ristoranti.add(ristoranteDAO.findById(sedi.get(i).getID_Ristorante()));
            System.out.println(ristoranti.get(i).getNome());
        }

        // cerco le recensioni per il piatto
        RecensioneDAO recensioneDAO = DatabaseDAO.getRecensioneDAO(null);
        List<Recensione> recensioni = new ArrayList<>();

        recensioni = recensioneDAO.findByPiatto(piatto.getId());
        for (int i = 0; i < recensioni.size(); i++){
            if (recensioni.get(i).getCommento() == null)
                recensioni.get(i).setCommento("");
        }

        List<User> utenti_recensori = new ArrayList<>();
        for (int i = 0; i < recensioni.size(); i++){
            CF_utente = recensioni.get(i).getUtenteR().getCF();
            // System.out.println(CF_utente);
            utenti_recensori.add(userDAO.findByCF(CF_utente));
            //utenti_recensori.set(i, userDAO.findByCF(CF_utente));
            // System.out.println(utenti_recensori.get(i));
        }



        DatabaseDAO.closeTransaction();

        //page.addObject("id_piatto", id_piatto);
        //page.addObject("nome_piatto", nome_piatto2);
        page.addObject("piatto_passato", piatto);
        page.addObject("ingredienti", contiene);
        page.addObject("utente_post", utente_post);
        page.addObject("sedi", sedi);
        page.addObject("ristoranti", ristoranti);
        page.addObject("recensioni", recensioni);
        page.addObject("utenti_recensori", utenti_recensori);

        return page;
    }
}
