package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.dataAccessObjects.Cookie.CookieDAOFactory;
import com.project.chefskiss.dataAccessObjects.Database.MySQLJDBC_DAOFactory;
import com.project.chefskiss.dataAccessObjects.Database.ValutazioneDAO_MySQL;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpResponse;
import java.util.Map;

public abstract class DAOFactory {
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    public abstract IngredienteDAO getIngredienteDAO(HttpServletResponse response);

    public abstract UserDAO getUserDAO(HttpServletResponse response);

    public abstract PiattoDAO getPiattoDAO(HttpServletResponse response);

    public abstract RistoranteDAO getRistoDAO(HttpServletResponse response);

    public abstract SedeDAO getSedeDAO(HttpServletResponse response);

    public abstract ContieneDAO getContieneDAO(HttpServletResponse response);

    public abstract RecensioneDAO getRecensioneDAO(HttpServletResponse response);

    public abstract ValutazioneDAO getValutazioneDAO(HttpServletResponse response);

    public static DAOFactory getDAOFactory(String whichFactory, HttpServletResponse response) {

        if (whichFactory.equals("MySQLJDBCImpl")) {
            return new MySQLJDBC_DAOFactory();
        } else if (whichFactory.equals("CookieImpl")) {
            return new CookieDAOFactory(response);
        } else {
            return null;
        }
    }
}
