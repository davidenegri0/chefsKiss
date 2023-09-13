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
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email, String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef, Boolean Se_Ristoratore, Boolean deleted, String Coordinate_Sede) {
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
        User utente = new User();

        try {
            String SQLQuery = "SELECT * "
                            + "FROM Utente "
                            + "WHERE Email = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Email);

            ResultSet result = query.executeQuery();

            if(result.next())
            {
                utente = read(result);
                System.out.println("Lettura dati completata!");
            }
            result.close();
            query.close();

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente;
    }

    private User read(ResultSet rs) {
        User user = new User();

        try {
            user.setCF(rs.getString("CF"));
        } catch (SQLException sqle) { }
        try {
            user.setNome(rs.getString("Nome"));
        } catch (SQLException sqle) { }
        try {
            user.setCognome(rs.getString("Cognome"));
        } catch (SQLException sqle) { }
        try {
            user.setD_Nascita(rs.getDate("Data_Nascita"));
        } catch (SQLException sqle) { }
        try {
            user.setEmail(rs.getString("Email"));
        } catch (SQLException sqle) { }
        try {
            user.setPassword(rs.getString("Password"));
        } catch (SQLException sqle) { }
        try {
            user.setN_Telefono(rs.getString("Telefono"));
        } catch (SQLException sqle) { }
        try {
            user.setD_Iscrizione(rs.getDate("Data_Iscrizione"));
        } catch (SQLException sqle) { }
        /*      //TODO: Quando i dati sul db sono tutti completi, decommentare
        try {
            user.setDeleted(rs.getString("Deleted").equals("Y"));
        } catch (SQLException sqle) { }

        //TODO: Finire di inserire la lettura per tutti i campi Se_* e campi correlati
        try {
            user.setPrivileges(rs.getString("Se_Cliente").equals("1"), rs.getString("Verificato").equals("1"), rs.getString("Se_Privato").equals("1"), rs.getString("Se_Chef").equals("1"), rs.getString("Se_Ristorante").equals("1"));
        } catch (SQLException sqle) { }
        try {
            if (rs.getString("Se_Privato").equals("Y")){
                user.setUsername(rs.getString("Username"));
                //TODO: Foto_Privato
            }
        } catch (SQLException sqle) { }
        try {
            if (rs.getString("Se_Chef").equals("Y")){
                //TODO: Foto_chef e CV
            }
        } catch (SQLException sqle) { }
         */

        return user;
    }
}
