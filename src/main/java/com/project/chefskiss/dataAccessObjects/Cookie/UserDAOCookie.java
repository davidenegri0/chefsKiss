package com.project.chefskiss.dataAccessObjects.Cookie;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;
import com.project.chefskiss.modelObjects.Sede;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Date;
import java.util.List;

public class UserDAOCookie implements UserDAO {
    private HttpServletResponse response;

    public UserDAOCookie(HttpServletResponse response)
    {
        this.response = response;
    }

    @Override
    public User create(String CF,
                       String Nome,
                       String Cognome,
                       Date D_Nascita,
                       String Email,
                       String Password,
                       String N_Telefono,
                       Date D_Iscrizione,
                       Boolean Se_Cliente,
                       Boolean Verificato,
                       Boolean Se_Privato,
                       String Username,
                       Boolean Se_Chef,
                       Boolean Se_Ristoratore,
                       Boolean Deleted,
                       Sede sede
    )throws UserAlreadyKnownException {
        User loggedUser = new User();
        loggedUser.setTotalData(Nome, Cognome, CF, Email, N_Telefono, D_Nascita, Password);
        loggedUser.setD_Iscrizione(D_Iscrizione);
        loggedUser.setPrivileges(Se_Cliente, Verificato, Se_Privato, Se_Chef, Se_Ristoratore);
        loggedUser.setUsername(Username);
        loggedUser.setDeleted(Deleted);

        Cookie cookie;
        cookie = new Cookie("loggedUser", User.encodeUserData(loggedUser));
        cookie.setPath("/");
        cookie.setMaxAge(Config.COOKIE_EXPIRATION_TIME);
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
    public void updateUserPassword(User user, String newPassword) {

    }

    @Override
    public User findByCF(String CF) {
        return null;
    }       //Unused operation

    @Override
    public User findByCF1(String CF) {
        return null;
    }       //Unused operation

    @Override
    public User findByNomeCognome(String Nome, String Cognome) {
        return null;
    }       //Unused operation

    @Override
    public User findByEmail(String Email) { return null; } //Unused operation

    @Override
    public List<User> findBySede(Sede sede){ return null; } //Unused operation

    @Override
    public List<User> getAllFreeChefs() {       //Unused operation
        return null;
    }
    /*
    public void createProfileImg(User user){
        try{
            Blob picture = user.getProfilePicture();
            byte[] pictureBytes = picture.getBytes(1,(int)picture.length());
            String encodedPicture = Base64.getEncoder().encodeToString(pictureBytes);
            //System.out.println(encodedPicture);
            Cookie cookie = new Cookie("userProfilePicture", encodedPicture);
            cookie.setPath("/");
            cookie.setMaxAge(Config.COOKIE_EXPIRATION_TIME);
            response.addCookie(cookie);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
     */

}
