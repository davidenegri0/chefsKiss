package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.dataAccessObjects.Database.MySQLJDBC_DAOFactory;

import java.net.http.HttpResponse;
import java.util.Map;

public abstract class DAOFactory {
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    // public abstract UserDAO getUserDAO();

    public static DAOFactory getDAOFactory(String whichFactory, HttpResponse response) {

        if (whichFactory.equals("MySQLJDBCImpl")) {
            return new MySQLJDBC_DAOFactory();
        } else if (whichFactory.equals("CookieImpl")) {
            System.out.println("TODO: Cookie");
            //return new CookieDAOFactory();
        } else {
            return null;
        }
        return null;
    }
}
