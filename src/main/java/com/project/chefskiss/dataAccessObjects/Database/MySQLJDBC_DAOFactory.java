package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.*;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.validation.Validator;
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
    public IngredienteDAO getIngredienteDAO(HttpServletResponse response) {
        return new IngredienteDAO_MySQL(conn);
    }


    @Override
    public UserDAO_MySQL getUserDAO(HttpServletResponse response)
    {
        return new UserDAO_MySQL(conn);
    }

    @Override
    public PiattoDAO getPiattoDAO(HttpServletResponse response) {
        return new PiattoDAO_MySQL(conn);
    }

    @Override
    public RistoranteDAO_MySQL getRistoDAO(HttpServletResponse response) {
        return new RistoranteDAO_MySQL(conn);
    }

    @Override
    public SedeDAO getSedeDAO(HttpServletResponse response) {
        return new SedeDAO_MySQL(conn);
    }

    public RecensioneDAO getRecensioneDAO(HttpServletResponse response){ return new RecensioneDAO_MySQL(conn); };

    @Override
    public ContieneDAO getContieneDAO(HttpServletResponse response) {
        return new ContieneDAO_MySQL(conn);
    }

    @Override
    public ValutazioneDAO getValutazioneDAO(HttpServletResponse response) {
        return new ValutazioneDAO_MySQL(conn);
    }

    @Override
    public PrenotazioneDAO getPrenotazioneDAO(HttpServletResponse response) {
        return new PrenotazioneDAO_MySQL(conn);
    }

}
