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

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente;
    }

    private User read(ResultSet rs)
    throws SQLException
    {
        User user = new User();

        //Ho modificato la lettura, rimuovendo i blocchi try catch, non servono visto che l'eccezioni vengono gestite nel blocco superiore
        //Ho inserito un paio di try catch per settare a false tutti i parametri che sono nulli nel db nel valore di ritorno

        user.setCF(rs.getString("CF"));
        user.setNome(rs.getString("Nome"));
        user.setCognome(rs.getString("Cognome"));
        user.setD_Nascita(rs.getDate("Data_Nascita"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));
        user.setN_Telefono(rs.getString("Telefono"));
        user.setD_Iscrizione(rs.getDate("Data_Iscrizione"));
        try {
            user.setDeleted(rs.getString("Deleted").equals("Y"));
        } catch (NullPointerException e)
        {
            user.setDeleted(false);
        }
        try {
            user.setPrivileges(
                    rs.getInt("Se_Cliente")==1,
                    rs.getInt("Verificato")==1,
                    rs.getInt("Se_Privato")==1,
                    rs.getInt("Se_Chef")==1,
                    rs.getInt("Se_Ristoratore")==1
            );
        } catch (NullPointerException e)
        {
            user.setPrivileges(false, false, false, false, false);
        }

        if (user.isPrivato()) {
            user.setUsername(rs.getString("Username"));
            user.setProfilePicture(rs.getBlob("Foto_Privato"));
        }
        if (user.isChef()){
            //TODO: Foto_chef e CV
        }
        if (user.isRistoratore()){
            //TODO: Coordinate ristoranti
        }

        return user;
    }
}
