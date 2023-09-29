package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PiattoDAO_MySQL implements PiattoDAO {
    private Connection conn;

    public PiattoDAO_MySQL(Connection conn) { this.conn = conn; }
    @Override
    public Piatto create(String Nome_Piatto, String Preparazione, User Utente)
    {
        PreparedStatement query;
        Piatto piatto = new Piatto();
        Date data_ora = new Date();
        Timestamp timestamp = new Timestamp(data_ora.getTime());

        try {
            String SQLQuery = "INSERT INTO chefskiss.piatto(Nome_Piatto, Data_Upload, Preparazione, CF) VALUES (?, ?, ?, ?) ";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Piatto);
            query.setTimestamp(2, timestamp);
            if (Preparazione==null) query.setNull(3, Types.VARCHAR);
            else query.setString(3, Preparazione);
            query.setString(4, Utente.getCF());

            query.executeUpdate();

            query.close();

            SQLQuery = "SELECT * FROM chefskiss.piatto WHERE Nome_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Piatto);
            ResultSet result = query.executeQuery();
            if (result.next()){
                piatto = read(result);
            }

            result.close();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatto;
    }

    @Override
    public void update(Piatto piatto) {
        PreparedStatement query;
        Integer id_piatto = piatto.getId();

        try {
            String SQLQuery = "UPDATE chefskiss.piatto SET Nome_Piatto = ?, Preparazione = ?, CF = ? WHERE ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, piatto.getNome());
            query.setString(2, piatto.getPreparazione());
            query.setString(3, piatto.getUtenteP().getCF());
            query.setInt(4, id_piatto);

            query.executeUpdate();

            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Piatto piatto) {
        PreparedStatement query;
        Integer ID_Piatto = piatto.getId();

        try{
            String SQLQuery = "UPDATE chefskiss.piatto SET Deleted = 'Y' WHERE ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, ID_Piatto);

            query.executeUpdate();

            String Verifica = "SELECT Deleted FROM chefskiss.piatto WHERE ID_Piatto = ?";
            query = conn.prepareStatement(Verifica);
            query.setInt(1, ID_Piatto);
            ResultSet result2 = query.executeQuery();

            while (result2.next()){
                String deleted = result2.getString("Deleted");
                if (deleted.equals('Y')) System.out.println("Cancellazione effettuata con successo!");
                else System.out.println("Errore durante la cancellazione dell'utente");
            }
            result2.close();

            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addPiattoinSede (Piatto piatto, Sede sede){
        PreparedStatement query;

        try{
            String SQLQuery = "INSERT INTO chefskiss.servito_in (ID_Piatto, Coordinate) VALUES (?,?)";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, piatto.getId());
            query.setString(2, sede.getCoordinate());

            query.executeUpdate();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Piatto> findByName(String Nome_Piatto) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM chefskiss.piatto p " +
                    "LEFT JOIN chefskiss.recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "WHERE p.Nome_Piatto LIKE ?" +
                    "GROUP BY p.ID_Piatto";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+Nome_Piatto+"%");

            ResultSet result = query.executeQuery();

            while (result.next()) {
                Piatto p = read(result);
                p.setVotoMedio(result.getFloat("Media"));
                piatti.add(p);

            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        System.out.println("Lettura dati completata!");
        return piatti;
    }

    @Override
    public List<Piatto> findByIngediente(Ingrediente Ingrediente) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        String ingrediente = Ingrediente.getNome();

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM chefskiss.piatto p " +
                    "LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "JOIN contiene c on p.ID_Piatto = c.ID_Piatto " +
                    "WHERE c.Nome_Ingrediente LIKE ? " +
                    "GROUP BY p.ID_Piatto";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+ingrediente+"%");

            ResultSet result = query.executeQuery();

            while (result.next()) {
                Piatto p = read(result);
                p.setVotoMedio(result.getFloat("Media"));
                piatti.add(p);
            }

            result.close();
            query.close();

            System.out.println("Lettura dati completata!");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }

    @Override
    public List<Piatto> findBySede(Sede Sede) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        String coordSede = Sede.getCoordinate();

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM piatto p " +
                    "LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "JOIN servito_in si on p.ID_Piatto = si.ID_Piatto " +
                    "WHERE si.Coordinate = ? " +
                    "GROUP BY p.ID_Piatto";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, coordSede);

            ResultSet result = query.executeQuery();

            while (result.next()) {
                Piatto p = read(result);
                p.setVotoMedio(result.getFloat("Media"));
                piatti.add(p);
            }

            result.close();
            query.close();

            System.out.println("Lettura dati completata!");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }

    @Override
    public List<Piatto> findByCF(User CF_Utente) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        String cf = CF_Utente.getCF();

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM piatto p " +
                    "LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "WHERE p.CF = ? " +
                    "GROUP BY p.ID_Piatto";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, cf);

            ResultSet result = query.executeQuery();

            while (result.next()) {
                Piatto p = read(result);
                p.setVotoMedio(result.getFloat("Media"));
                piatti.add(p);
            }

            System.out.println("Lettura dati completata!");
            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }

    @Override
    public Piatto findByIDPiatto(Integer ID_Piatto) {
        PreparedStatement query;
        Piatto piatto = new Piatto();

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM piatto p " +
                    "LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "WHERE p.ID_Piatto = ? " +
                    "GROUP BY p.ID_Piatto";
            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, ID_Piatto);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                piatto = read(result);
                piatto.setVotoMedio(result.getFloat("Media"));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatto;
    }

    @Override
    public List<Piatto> findMostRecent(int num) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        int i;

        try {
            String SQLQuery =
                    "SELECT p.*, AVG(r.Voto) as Media " +
                    "FROM piatto p " +
                    "LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto " +
                    "GROUP BY p.ID_Piatto, p.Data_Upload " +
                    "ORDER BY p.Data_Upload;";
            query = conn.prepareStatement(SQLQuery);

            ResultSet result = query.executeQuery();

            for (i = 0; result.next() && i<num; i++) {
                Piatto p = read(result);
                p.setVotoMedio(result.getFloat("Media"));
                piatti.add(p);
            }

            if (i!=num) System.out.println("Piatti ricevuti inferiori al numero richiesto");

            System.out.println("Lettura dati piatti completata!");
            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;

    }

    private Piatto read (ResultSet rs) throws SQLException
    {
        Piatto piatto = new Piatto();

        piatto.setID(rs.getInt("ID_Piatto"));
        piatto.setNome(rs.getString("Nome_Piatto"));
        piatto.setPreparazione(rs.getString("Preparazione"));

        User utente = new User();
        piatto.setUtenteP(utente);
        piatto.getUtenteP().setCF(rs.getString("CF"));

        piatto.setDeleted(rs.getBoolean("Deleted"));

        System.out.println("Dati letti del piatto: "+piatto.getId());
        return piatto;
    }
}
