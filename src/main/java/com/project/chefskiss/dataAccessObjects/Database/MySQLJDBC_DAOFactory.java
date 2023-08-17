package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.DAOFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLJDBC_DAOFactory extends DAOFactory {
    private Connection conn;

    @Override
    public void beginTransaction() {
        try {
            Class.forName(Config.DATABASE_DRIVER);
            this.conn = DriverManager.getConnection(Config.DATABASE_URL);
            this.conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitTransaction() {
        try {
            this.conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            this.conn.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeTransaction() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public UserDAO_MySQL getUserDAO()
    {
        return new UserDAO_MySQL(conn);
    }

}
