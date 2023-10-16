package com.project.chefskiss.controllers;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.Utility;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

@Controller
public class registrationController {

    @GetMapping(path = "/registration")
    public ModelAndView viewRegistrationPage(){
        return new ModelAndView("registrationPage");
    }

    @PostMapping(value = "/registration",
            params = {"nome", "cognome", "cf", "email", "telefono", "nascita", "pssw"})
    public ModelAndView showRegistrationComplete(
            HttpServletResponse response,
            @RequestParam(name = "nome") String Nome,
            @RequestParam(name = "cognome") String Cognome,
            @RequestParam(name = "cf") String CF,
            @RequestParam(name = "email") String Email,
            @RequestParam(name = "telefono") String Telefono,
            @RequestParam(name = "nascita") String Data,
            @RequestParam(name = "pssw") String Password,
            @RequestParam(value = "cliente", required = false, defaultValue = "false") boolean isCliente,
            @RequestParam(name = "privato", required = false, defaultValue = "false") boolean isPrivato,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "foto_prv", required = false) MultipartFile foto_prv,
            @RequestParam(name = "chef", required = false, defaultValue = "false") boolean isChef,
            @RequestParam(name = "foto_chef", required = false) MultipartFile foto_chef,
            @RequestParam(name = "cv_chef", required = false) MultipartFile cv,
            @RequestParam(name = "ristoratore", required = false, defaultValue = "false") boolean isRistoratore,
            @RequestParam(name = "nome_rist", required = false) String nomeRist
            ){
        // Variabli
        ModelAndView page;
        User utente = null;

        page = new ModelAndView("index");

        if(isPrivato && foto_prv!=null && foto_prv.getSize()>64000){
            page.setViewName("registrationPage");
            page.addObject("errorCode", 2);
            return page;
        }

        if(isChef && foto_chef!=null && foto_chef.getSize()>64000){
            page.setViewName("registrationPage");
            page.addObject("errorCode", 2);
            return page;
        }

        // Connesione al database
        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        try {
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);

            utente = sessionUserDAO.create(CF, Nome, Cognome, Date.valueOf(Data), Email, Password, Telefono, Date.valueOf(LocalDate.now()),
                    isCliente, false, isPrivato, null, isChef, isRistoratore, false, null);

            DatabaseDAO.commitTransaction();

            if (isPrivato){
                utente.setUsername(username);
                utente.setProfilePicture(new SerialBlob(foto_prv.getBytes()));
            }
            if (isChef){
                utente.setChefPicture(new SerialBlob(foto_chef.getBytes()));
                utente.setChefCV(new SerialClob(new String(cv.getBytes()).toCharArray()));
            }
            if (isPrivato || isChef){
                sessionUserDAO.update(utente);
            }

            DatabaseDAO.commitTransaction();

        } catch (Exception e){
            e.printStackTrace();
            DatabaseDAO.rollbackTransaction();
            page.setViewName("registrationPage");
            page.addObject("errorCode", 1);
            return page;
        }

        DatabaseDAO.closeTransaction();

        //Creazione cookie utente
        try {
            DAOFactory CookieDAO = DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response);
            UserDAO userCookie = CookieDAO.getUserDAO(response);
            userCookie.create(
                    utente.getCF(),
                    utente.getNome(),
                    utente.getCognome(),
                    utente.getD_Nascita(),
                    utente.getEmail(),
                    "redacted",
                    utente.getN_Telefono(),
                    Date.valueOf(LocalDate.now()),
                    isCliente,
                    false,
                    isPrivato,
                    username,
                    isChef,
                    isRistoratore,
                    false,
                    null
            );
        } catch (UserAlreadyKnownException e){
            System.out.println("Come minchia è possibile?");
        }

        page.addObject("user", utente);

        if (isRistoratore){
            return Utility.redirect(page, "/addRistorante&Sede");
        }

        return page;
    }
}
