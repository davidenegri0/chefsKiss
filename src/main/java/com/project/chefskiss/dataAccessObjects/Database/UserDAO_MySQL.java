package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.*;

public class UserDAO_MySQL implements UserDAO {
    Connection conn;

    public UserDAO_MySQL(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email, String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef, Boolean Se_Ristoratore, Boolean deleted, Sede sede)
    throws UserAlreadyKnownException
    {
        // togliere i se_*

        PreparedStatement query;
        User utente = new User();
        utente.setSedeU(sede);

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

            String SQLQuery2 = "INSERT INTO chefskiss.utente(" +
                    "CF, " + // 1
                    "Nome, " + // 2
                    "Cognome, " + // 3
                    "Data_Nascita, " + // 4
                    "Email, " + // 5
                    "Password, " + // 6
                    "Telefono, " + // 7
                    "Data_Iscrizione, " + // 8
                    "Coordinate) " + // 9
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            int i = 1;
            query = conn.prepareStatement(SQLQuery2);
            query.setString(i++, CF);
            query.setString(i++, Nome);
            query.setString(i++, Cognome);
            query.setDate(i++, D_Nascita);
            query.setString(i++, Email);
            query.setString(i++, Password);
            query.setString(i++, N_Telefono);
            query.setDate(i++, D_Iscrizione);
            query.setString(i, utente.getSedeU().getCoordinate());

            query.executeUpdate();

            query.close();

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente.setTotalData(Nome, Cognome, CF, Email, N_Telefono, D_Nascita, Password);
    }

    @Override
    public void update(User user) {
        PreparedStatement query;
        PreparedStatement query2;

        // TODO: Attualmente, la update non è in grado di aggiornare la password. Da effettuare in un metodo specifico
        // TODO: In realtà, converrebbe farlo per ogni sotto-query

        try {
            String SQLQuery = "UPDATE chefskiss.utente " +
                    "SET Nome = ?, " + // 1
                    "Cognome = ?, " + // 2
                    "Data_Nascita = ?, " + // 3
                    "Email = ?, " + // 4

                    //"Password = ?, " + // 5

                    "Telefono = ?, " + // 5
                    "Se_Cliente = ?, " + // 6
                    "Se_Privato = ?, " + // 7
                    "Se_Chef = ?, " + // 8
                    "Se_Ristoratore = ? " + // 9
                    "WHERE CF = ?"; //10

            int i = 1;
            query = conn.prepareStatement(SQLQuery);
            query.setString(i++, user.getNome());
            query.setString(i++, user.getCognome());
            query.setDate(i++, user.getD_Nascita());
            query.setString(i++, user.getEmail());

            //query.setString(5, user.getPassword());

            query.setString(i++, user.getN_Telefono());
            if (user.isCliente()) {
                query.setInt(i++, 1);

                if (user.isClienteVerificato()) {
                    String SQLQuery2 = "UPDATE chefskiss.utente SET Verificato = ? WHERE CF = ?";

                    query2 = conn.prepareStatement(SQLQuery2);
                    query2.setInt(1, 1);
                    query2.setString(2, user.getCF());

                    query2.executeUpdate();
                    query2.close();
                }
            }
            else query.setInt(i++, 0);
            if (user.isPrivato()) {
                query.setInt(i++, 1);

                if (user.getProfilePicture()!=null)
                {
                    String SQLQuery2 =
                            "UPDATE chefskiss.utente " +
                                    "SET Username = ?," +
                                    "Foto_Privato = ? " +
                                    "WHERE CF = ?";
                    query2 = conn.prepareStatement(SQLQuery2);
                    query2.setString(1, user.getUsername());
                    query2.setBlob(2, user.getProfilePicture());
                    query2.setString(3, user.getCF());
                }
                else
                {
                    String SQLQuery2 =
                            "UPDATE chefskiss.utente " +
                            "SET Username = ? " +
                            "WHERE CF = ?";
                    query2 = conn.prepareStatement(SQLQuery2);
                    query2.setString(1, user.getUsername());
                    query2.setString(2, user.getCF());
                }

                query2.executeUpdate();
                query2.close();
            }
            else query.setInt(i++, 0);
            if (user.isChef()) {
                query.setInt(i++, 1);

                String SQLQuery2 = "UPDATE chefskiss.utente SET Foto_Chef = ?, CV = ?, Coordinate = ? WHERE CF = ?";

                query2 = conn.prepareStatement(SQLQuery2);
                query2.setBlob(1, user.getProfilePicture()); // ?????
                //query2.setBlob(2, user.getCV()); // ????? caricare il cv
                query2.setNull(2, Types.BLOB); //Per ora non imposta nulla ---> TODO: Rimuovere questa line dopo aver implementato
                query2.setString(3, user.getSedeU().getCoordinate()); // coordinate c'è solo se l'utente è chef
                query2.setString(4, user.getCF());

                query2.executeUpdate();
                query2.close();

                // query.setBlob(13, user.getFoto_Chef()); TODO: getter e setter
                // query.setClob(14, user.getCV());
            }
            else query.setInt(i++, 0);
            if (user.isRistoratore()) query.setInt(i++, 1);
            else query.setInt(i++, 0);

            query.setString(i, user.getCF());

            query.executeUpdate();

            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(User user) {
        PreparedStatement query;
        String CF = user.getCF();

        try{
            String SQLQuery = "UPDATE chefskiss.utente SET Deleted = 'Y' WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            query.executeUpdate();

            String Verifica = "SELECT Deleted FROM chefskiss.utente WHERE CF = ?";
            query = conn.prepareStatement(Verifica);
            query.setString(1, CF);
            ResultSet result2 = query.executeQuery();

            while (result2.next()){
                String deleted = result2.getString("Deleted");
                if (deleted.equals('Y')) System.out.println("Cancellazione effettuata con successo!");
                else System.out.println("Errore durante la cancellazione dell'utente");
            }
            result2.close();

            query.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User findByCF(String CF) {
        PreparedStatement query;
        User utente = new User();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.utente WHERE CF = ? AND Deleted = 'N'";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            ResultSet result = query.executeQuery();

            if(result.next())
            {
                utente = read(result);
                System.out.println("Lettura dati completata!");
            }
            result.close();
            query.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente;
    }

    @Override
    public User findByNomeCognome(String Nome, String Cognome) {

        PreparedStatement query;
        User utente = new User();

        try{
            String SQLQuery = "SELECT * FROM Utente WHERE Nome = ? AND Cognome = ? AND Deleted = 'N'";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome);
            query.setString(2, Cognome);

            ResultSet result = query.executeQuery();

            if(result.next())
            {
                utente = read(result);
                System.out.println("Lettura dati completata!");
            }
            result.close();
            query.close();
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utente;
    }

    @Override
    public User findByEmail(String Email) {
        PreparedStatement query;
        User utente = new User();

        try {
            String SQLQuery = "SELECT * "
                            + "FROM Utente "
                            + "WHERE Email = ? AND Deleted = 'N'";

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
        Sede sede = new Sede();
        user.setSedeU(sede);

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
            user.getSedeU().setCoordinate(rs.getString("Coordinate"));
        }

        return user;
    }
}
