package com.project.chefskiss.dataAccessObjects;

import java.util.Map;

public abstract class DAOFactory {
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    // public abstract UserDAO getUserDAO();

    public static DAOFactory getDAOFactory(String whichFactory, Map factoryParameters) {

        if (whichFactory.equals("MySQLJDBCImpl")) {
            //return new MySQLJDBCDAOFactory(factoryParameters);
            System.out.println("Hello database");
        } else if (whichFactory.equals("CookieImpl")) {
            System.out.println("Hello cookie");
            //return new CookieDAOFactory(factoryParameters);
        } else {
            return null;
        }
        return null;
    }
}
