package com.project.chefskiss.dataAccessObjects.Cookie;

import com.project.chefskiss.dataAccessObjects.*;
import com.project.chefskiss.dataAccessObjects.Database.ContieneDAO_MySQL;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ResponseBody;

public class CookieDAOFactory extends DAOFactory {
    HttpServletResponse response;

    public CookieDAOFactory( HttpServletResponse response){
        this.response = response;
    }
    @Override
    public void beginTransaction() {

    }

    @Override
    public void commitTransaction() {

    }

    @Override
    public void rollbackTransaction() {

    }

    @Override
    public void closeTransaction() {

    }

    @Override
    public IngredienteDAO getIngredienteDAO(HttpServletResponse response) {
        return null;
    }
    //UNUSED

    @Override
    public UserDAO getUserDAO(HttpServletResponse response) {
        return new UserDAOCookie(response);
    }

    @Override
    public PiattoDAO getPiattoDAO(HttpServletResponse response) {
        return null;
    }
    //UNUSED

    @Override
    public RistoranteDAO getRistoDAO(HttpServletResponse response) {
        return null;
    }
    //UNUSED

    @Override
    public SedeDAO getSedeDAO(HttpServletResponse response) {
        return null;
    }
    //UNUSED

    @Override
    public ContieneDAO getContieneDAO(HttpServletResponse response) { return null; }
    //UNUSED

    @Override
    public RecensioneDAO getRecensioneDAO(HttpServletResponse response) { return null; };
    //UNUSED
}
