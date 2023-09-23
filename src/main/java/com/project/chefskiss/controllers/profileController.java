package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.Database.MySQLJDBC_DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

@Controller
public class profileController {

    //Il path in Windows deve essere con i backslash e dev'essere assoluto (il relativo sembra non funzionare)
    //private final String profileImgPath = "..\\..\\..\\resources\\static\\profileImg.jpg";

    //Handler della richiesta di visualizzazione del profilo
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView onProfileViewRequest(
            HttpServletResponse response,
            @CookieValue("loggedUser") String userData
    ){
        ModelAndView page = new ModelAndView("profilePage");

        //Caricamento dati dai cookie
        User utente = User.decodeUserData(userData);
        page.addObject("utente", utente);
        page.addObject("imgPath", "profile/profileImg.jpg");

        return page;
    }

    //Aggiornamento immagine del profilo
    //TODO: Spostare funzionalità nella pagina di aggiornamento profilo
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView profilePageEdit(
            @CookieValue("loggedUser") String userData,
            @RequestParam("file") MultipartFile file
    ){
        ModelAndView page = new ModelAndView("profilePage");
        User utente = User.decodeUserData(userData);

        try{
            /*
                QUESTO LAVORA SU FILE --> OUTDATED

                File outputFile = new File(Config.PROFILE_IMG_PATH);
                FileOutputStream out = new FileOutputStream(outputFile);
                out.write(file.getBytes());
                System.out.println(outputFile.getAbsolutePath());
                out.close();
             */

            Blob sqlImage = null;
            sqlImage.setBytes(1, file.getBytes());
            utente.setProfilePicture(sqlImage);

            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);

            sessionUserDAO.update(utente); //---> Per ora non fa niente //TODO: Caricare l'immagine sul db

            DatabaseDAO.commitTransaction();
            DatabaseDAO.closeTransaction();

        } catch (IOException | NullPointerException | SQLException e)
        {
            e.printStackTrace();
        }
        page.addObject("imgPath", "profile/profileImg.jpg");
        page.addObject("utente", utente);
        return page;
    }

    //Handler della richiesta di aggiornamento del profilo
    @RequestMapping(value = "updateProfile", method = RequestMethod.GET)
    public ModelAndView updateProfileRequest(
            @CookieValue("loggedUser") String userData
    )
    {
        ModelAndView page = new ModelAndView("updatePage");

        //Caricamento dati dai cookie
        User utente = User.decodeUserData(userData);
        page.addObject("utente", utente);
        page.addObject("imgPath", "profile/profileImg.jpg");
        return page;
    }

    //Aggiornamento del profilo effettivo
    @RequestMapping(value = "updateProfile", method = RequestMethod.POST)
    public ModelAndView updateProfile(
            @RequestParam(name = "email", defaultValue = "") String email,
            @RequestParam(name = "tel", defaultValue = "") String telefono,
            @RequestParam(name = "username", defaultValue = "") String username,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @CookieValue("loggedUser") String userData,
            HttpServletResponse response
    )
    {
        //Caricamento dati dai cookie
        ModelAndView page = new ModelAndView("profilePage");
        User utente = User.decodeUserData(userData);
        utente.setPassword(null);

        //Caricamento immagine dal db
        DAOFactory DatabaseDAO2 = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO2.beginTransaction();
        UserDAO sessionUserDAO2 = DatabaseDAO2.getUserDAO(null);
        Blob sqlImage = sessionUserDAO2.findByEmail(utente.getEmail()).getProfilePicture();
        DatabaseDAO2.closeTransaction();

        //Aggiornamento dati con modifiche inserite
        if (!email.isEmpty()){
            utente.setEmail(email);
        }
        if (!telefono.isEmpty()){
            utente.setN_Telefono(telefono);
        }
        if (!username.isEmpty()){
            utente.setUsername(username);
        }

        //Aggiornamento immagine di profilo
        if (!file.isEmpty())
        {
            if (file.getSize()< 64000) {
                try {
                    sqlImage = new SerialBlob(file.getBytes());
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Il file ha dimensioni troppo grandi, no changes");
                page.addObject("errorCode", 100);
                page.setViewName("updatePage");
            }
        }
        else
        {
            System.out.println("Il file è nullo, no changes");


        }
        utente.setProfilePicture(sqlImage);

        //Invio dati al db

        DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
        DatabaseDAO.beginTransaction();
        UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);

        sessionUserDAO.update(utente);

        DatabaseDAO.commitTransaction();
        DatabaseDAO.closeTransaction();

        //Aggiornamento cookie
        DAOFactory CookieDAO = DAOFactory.getDAOFactory(Config.COOKIE_IMPL, response);
        UserDAO userCookie = CookieDAO.getUserDAO(response);
        userCookie.update(utente);

        //Fine
        page.addObject("utente", utente);
        page.addObject("imgPath", "profile/profileImg.jpg");
        return page;
    }

    //Generazione dinamica dell'immagine del profilo
    @RequestMapping(value = "profile/profileImg.jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] showProfileImage(
            @CookieValue("loggedUser") String userData
    ){
        try {
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            String email = User.decodeUserData(userData).getEmail();
            Blob sqlImage = sessionUserDAO.findByEmail(email).getProfilePicture();;
            DatabaseDAO.closeTransaction();
            //FileInputStream in = new FileInputStream(Config.PROFILE_IMG_PATH);
            //return in.readAllBytes();
            return sqlImage.getBytes(1, (int)sqlImage.length());
        } catch (IllegalArgumentException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
