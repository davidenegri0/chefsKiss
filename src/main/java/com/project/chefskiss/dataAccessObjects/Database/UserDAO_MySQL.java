package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
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
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email, String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef, Boolean Se_Ristoratore, Boolean deleted, String Coordinate_Sede)
    throws UserAlreadyKnownException
    {

        PreparedStatement query;
        User utente = new User();

        try {
            String SQLQuery = "SELECT * FROM chefskiss.utente WHERE CF = ? OR Email = ? OR Telefono = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);
            query.setString(2, Email);
            query.setString(3, N_Telefono);

            ResultSet rs = query.executeQuery();

            if (rs.next()){
                throw new UserAlreadyKnownException(CF + " or " + Email + " or " + N_Telefono + " is already in the database");
            }

            rs.close();
            query.close();

            String SQLQuery2 = "INSERT INTO chefskiss.utente(CF, Nome, Cognome, Data_Nascita, Email, Password, Telefono, Data_Iscrizione, Se_Cliente, Se_Privato, Se_Chef, Se_Ristoratore) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            query = conn.prepareStatement(SQLQuery2);
            query.setString(1, CF);
            query.setString(2, Nome);
            query.setString(3, Cognome);
            query.setDate(4, D_Nascita);
            query.setString(5, Email);
            query.setString(6, Password);
            query.setString(7, N_Telefono);
            query.setDate(8, D_Iscrizione);
            if (Se_Cliente) query.setInt(9, 1);
            else query.setInt(9, 0);
            if (Se_Privato) query.setInt(10, 1);
            else query.setInt(10, 0);
            if (Se_Chef) query.setInt(11, 1);
            else query.setInt(11, 0);
            if (Se_Ristoratore) query.setInt(12, 1);
            else query.setInt(12, 0);

            query.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente.setTotalData(Nome, Cognome, CF, Email, N_Telefono, D_Nascita, Password);
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
