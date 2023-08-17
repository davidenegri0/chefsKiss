package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.User;

import java.sql.*;

public class UserDAO_MySQL implements UserDAO {
    Connection conn;

    public UserDAO_MySQL(Connection conn)
    {
        this.conn = conn;
    }

    //TODO: Tutte le funzioni seguenti
    @Override
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email, String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef, Boolean Se_Ristoratore, String Coordinate_Sede) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User findByCF(String CF) {
        return null;
    }

    @Override
    public User findByNomeCognome(String Nome, String Cognome) {
        return null;
    }

    @Override
    public User findByEmail(String Email) {
        PreparedStatement query;
        User utente = null;

        try {
            String SQLQuery = "SELECT * "
                            + "FROM Utente "
                            + "WHERE Email = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Email);

            ResultSet result = query.executeQuery();

            if(result.next())
            {
                System.out.println(result.getString("CF"));
                System.out.println(result.getString("Nome"));
                System.out.println(result.getString("Cognome"));

                //TODO: Setting dei dati dell'utente nella classe
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        //TODO: Return utente
        return utente;
    }
}
