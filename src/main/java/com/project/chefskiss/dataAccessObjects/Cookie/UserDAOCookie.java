package com.project.chefskiss.dataAccessObjects.Cookie;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpResponse;
import java.sql.Date;

public class UserDAOCookie implements UserDAO {
    private HttpServletResponse response;

    public UserDAOCookie(HttpServletResponse response)
    {
        this.response = response;
    }
    @Override
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email, String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef, Boolean Se_Ristoratore, Boolean Deleted, String Coordinate_Sede) throws UserAlreadyKnownException {
        User loggedUser = new User();
        loggedUser.setTotalData(Nome, Cognome, CF, Email, N_Telefono, D_Nascita, Password);

        Cookie cookie;
        cookie = new Cookie("loggedUser", User.encodeUserData(loggedUser));
        cookie.setPath("/");
        response.addCookie(cookie);

        return loggedUser;
    }

    @Override
    public void update(User user) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", User.encodeUserData(user));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void delete(User user) {
        Cookie cookie;
        cookie = new Cookie("loggedUser", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public User findByCF(String CF) {
        return null;
    }       //Unused operation

    @Override
    public User findByNomeCognome(String Nome, String Cognome) {
        return null;
    }       //Unused operation

    @Override
    public User findByEmail(String Email) {
        return null;
    }       //Unused operation
}
