package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.dataAccessObjects.UserDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO_MySQL implements UserDAO {
    Connection conn;

    public UserDAO_MySQL(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public User create(String CF, String Nome, String Cognome, Date D_Nascita, String Email,
                       String Password, String N_Telefono, Date D_Iscrizione, Boolean Se_Cliente,
                       Boolean Verificato, Boolean Se_Privato, String Username, Boolean Se_Chef,
                       Boolean Se_Ristoratore, Boolean deleted, Sede sede)
    throws UserAlreadyKnownException
    {
        PreparedStatement query;
        User utente = new User();
        utente.setSedeU(sede);

        try {
            String SQLQuery = "SELECT * FROM utente WHERE CF = ? OR Email = ? OR Telefono = ?";
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

            String SQLQuery2 = "INSERT INTO utente(" +
                    "CF, " + // 1
                    "Nome, " + // 2
                    "Cognome, " + // 3
                    "Data_Nascita, " + // 4
                    "Email, " + // 5
                    "Password, " + // 6
                    "Telefono, " + // 7
                    "Data_Iscrizione, " + //8
                    "Se_Cliente, " +    //9
                    "Verificato, " +    //10
                    "Se_Privato, " +    //11
                    "Username, " +    //12
                    "Se_Chef, " +   //13
                    "Se_Ristoratore, " + // 14
                    "Coordinate) " + // 15
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
            if (Se_Cliente) query.setInt(i++, 1);
            else query.setInt(i++, 0);
            if (Verificato) query.setInt(i++, 1);
            else query.setInt(i++, 0);
            if (Se_Privato) query.setInt(i++, 1);
            else query.setInt(i++, 0);
            if (Username == null || Username.isBlank() || !Se_Privato) query.setNull(i++, Types.VARCHAR);
            else query.setString(i++, Username);
            if (Se_Chef) query.setInt(i++, 1);
            else query.setInt(i++, 0);
            if (Se_Ristoratore) query.setInt(i++, 1);
            else query.setInt(i++, 0);
            if (sede==null) query.setNull(i, Types.VARCHAR);
            else query.setString(i, sede.getCoordinate());

            query.executeUpdate();

            query.close();

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        utente.setTotalData(Nome, Cognome, CF, Email, N_Telefono, D_Nascita, Password);
        utente.setPrivileges(Se_Cliente, Verificato, Se_Privato, Se_Chef, Se_Ristoratore);
        return utente;
    }

    @Override
    public void update(User user) {
        PreparedStatement query;
        PreparedStatement query2;

        //Il metodo update non è impostato per aggiornare la password --> Funzionalità spostata su funzione apposita

        try {
            String SQLQuery = "UPDATE utente " +
                    "SET Nome = ?, " + // 1
                    "Cognome = ?, " + // 2
                    "Data_Nascita = ?, " + // 3
                    "Email = ?, " + // 4
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

            //query.setString(i++, user.getPassword());

            query.setString(i++, user.getN_Telefono());
            if (user.isCliente()) {
                query.setInt(i++, 1);

                if (user.isClienteVerificato()) {
                    String SQLQuery2 = "UPDATE utente SET Verificato = ? WHERE CF = ?";

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
                            "UPDATE utente " +
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
                            "UPDATE utente " +
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

                String SQLQuery2 = "UPDATE utente SET Foto_Chef = ?, CV = ?, Coordinate = ? WHERE CF = ?";

                query2 = conn.prepareStatement(SQLQuery2);
                query2.setBlob(1, user.getChefPicture());
                query2.setClob(2, user.getChefCV());
                if (user.getSedeU()==null) query2.setNull(3, Types.VARCHAR);
                else query2.setString(3, user.getSedeU().getCoordinate()); // coordinate c'è solo se l'utente è chef
                query2.setString(4, user.getCF());

                query2.executeUpdate();
                query2.close();
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
            String SQLQuery = "UPDATE utente SET Deleted = 'Y' WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            query.executeUpdate();

            String Verifica = "SELECT Deleted FROM utente WHERE CF = ?";
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
    public void updateUserPassword(User user, String newPassword) {
        PreparedStatement query;

        try {
            String SQLQuery =
                    "UPDATE utente " +
                    "SET Password = ? " +
                    "WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, newPassword);
            query.setString(2, user.getCF());

            query.executeUpdate();

            query.close();
        } catch (SQLException e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User findByCF(String CF) {
        PreparedStatement query;
        User utente = new User();

        try{
            String SQLQuery = "SELECT * " +
                    "FROM utente " +
                    "WHERE CF = ? " +
                    "AND Deleted = 'N'";

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
    public User findByCF1(String CF) {
        PreparedStatement query;
        User utente = new User();

        try{
            String SQLQuery = "SELECT * " +
                    "FROM utente " +
                    "WHERE CF = ? ";

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
            String SQLQuery = "SELECT * " +
                    "FROM Utente " +
                    "WHERE Nome LIKE ? " +
                    "AND Cognome LIKE ? " +
                    "AND Deleted = 'N'";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+Nome+"%");
            query.setString(2, "%"+Cognome+"%");

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
            String SQLQuery =
                    "SELECT * " +
                    "FROM Utente " +
                    "WHERE Email = ? " +
                    "AND Deleted = 'N'";

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

    @Override
    public List<User> findBySede(Sede sede) {
        PreparedStatement query;
        List<User> utenti = new ArrayList<>();

        try {
            String SQLQuery =
                    "SELECT * " +
                    "FROM Utente " +
                    "WHERE Coordinate = ? " +
                    "AND Deleted = 'N'";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, sede.getCoordinate());

            ResultSet result = query.executeQuery();

            while (result.next())
            {
                utenti.add(read(result));
            }
            System.out.println("Lettura dati completata!");
            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utenti;
    }

    @Override
    public List<User> getAllFreeChefs() {
        PreparedStatement query;
        List<User> utenti = new ArrayList<>();

        try {
            String SQLQuary =
                    "SELECT * " +
                    "FROM utente " +
                    "WHERE Se_Chef = 1 " +
                    "AND Coordinate IS NULL";

            query = conn.prepareStatement(SQLQuary);
            ResultSet result = query.executeQuery();

            while (result.next()){
                utenti.add(read(result));
            }
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return utenti;
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
            user.setChefPicture(rs.getBlob("Foto_Chef"));
            user.setChefCV(rs.getClob("CV"));
            Sede s = new Sede();
            s.setCoordinate(rs.getString("Coordinate"));
            user.setSedeU(s);
        }

        return user;
    }
}
