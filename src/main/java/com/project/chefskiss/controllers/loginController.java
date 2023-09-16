package com.project.chefskiss.controllers;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class loginController {

    @RequestMapping(value = "/login")
    public ModelAndView showLogin(
            HttpServletResponse response,
            @RequestParam(name = "email", defaultValue = "",required = false) String email,
            @RequestParam(name = "pssw", defaultValue = "", required = false) String password,
            @CookieValue(value = "loggedUser", defaultValue = "") String userData
            )
    {
        // Variabili
        ModelAndView page = new ModelAndView("loginPage");
        User utente;

        // Controllo dati da login
        if(email.equals("") || password.equals("")){
            System.out.println("Empty fields");
            page.addObject("errorCode", 0);
        } else if (!userData.isEmpty()) {
            System.out.println("Already logged user");
            utente = User.decodeUserData(userData);
            page.setViewName("index");
            page.addObject("user", utente.getNome()+" "+utente.getCognome());
        } else
        {
            //Ricerca dei dati di login nel database
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            utente = sessionUserDAO.findByEmail(email);
            DatabaseDAO.closeTransaction();
            if (password.equals(utente.getPassword())){

                //Set del callback alla homepage
                System.out.println("Logged in");
                page.setViewName("index");
                page.addObject("user", utente.getNome()+" "+utente.getCognome());

                //Inserimento dati nei cookie TODO: Inserire la seconda parte dei dati
                try{
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
                            utente.getD_Iscrizione(),
                            utente.isCliente(),
                            utente.isClienteVerificato(),
                            utente.isPrivato(),
                            utente.getUsername(),
                            utente.isChef(),
                            utente.isRistoratore(),
                            utente.isDeleted(),
                            ""
                    );
                } catch (UserAlreadyKnownException e){
                    System.out.println("Come minchia è possibile?");
                }
            }
            else {
                System.out.println("Failed Logged in");
                page.addObject("errorCode", 1);
            }
        }

        return page;
    }

    @RequestMapping(value = "/logout")
    public ModelAndView onLogoutRequest(HttpServletResponse response){
        DAOFactory CookieDAO = DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response);
        UserDAO userCookie = CookieDAO.getUserDAO(response);
        userCookie.delete(null);   //---> L'eliminazione dei cookie non richiede nulla
        return new ModelAndView("index");
    }
}
