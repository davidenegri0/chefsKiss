package com.project.chefskiss.controllers;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

@Controller
public class chefResourcesController {
    @GetMapping(path = "/chef/{CF}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] showChefImage(
            @PathVariable("CF") String CF
    ){
        try {
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            Blob sqlImage = sessionUserDAO.findByCF(CF).getChefPicture();;
            DatabaseDAO.closeTransaction();
            return sqlImage.getBytes(1, (int)sqlImage.length());
        } catch (IllegalArgumentException | SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "/chef/{CF}/CV", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public byte[] downloadChefCV(
            @PathVariable("CF") String CF
    ){
        try {
            DAOFactory DatabaseDAO = DAOFactory.getDAOFactory(Config.DATABASE_IMPL, null);
            DatabaseDAO.beginTransaction();
            UserDAO sessionUserDAO = DatabaseDAO.getUserDAO(null);
            Clob sqlCV = sessionUserDAO.findByCF(CF).getChefCV();;
            DatabaseDAO.closeTransaction();
            return sqlCV.getAsciiStream().readAllBytes();
        } catch (SQLException | IOException | RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }
}
