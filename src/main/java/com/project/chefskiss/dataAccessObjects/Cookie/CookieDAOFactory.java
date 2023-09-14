package com.project.chefskiss.dataAccessObjects.Cookie;

import com.project.chefskiss.dataAccessObjects.DAOFactory;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import jakarta.servlet.http.HttpServletResponse;

public class CookieDAOFactory extends DAOFactory {
    HttpServletResponse response;

    public CookieDAOFactory(HttpServletResponse response){
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
    public UserDAO getUserDAO(HttpServletResponse response) {
        return new UserDAOCookie(response);
    }
}
