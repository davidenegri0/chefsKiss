package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.PiattoDAO;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PiattoDAO_MySQL implements PiattoDAO {
    Connection conn;

    public PiattoDAO_MySQL(Connection conn) { this.conn = conn; }
    @Override
    public Piatto create(String Nome_Piatto, String Preparazione, String CF_Utente)
    {
        PreparedStatement query;
        Piatto piatto = new Piatto();

        try {
            String SQLQuery = "INSERT INTO chefskiss.piatto(Nome_Piatto, Preparazione, CF) VALUES (?, ?, ?) ";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Piatto);
            query.setString(2, Preparazione);
            query.setString(3, CF_Utente);

            query.executeUpdate();

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
            query.setString(3, piatto.getCF_Utente());
            query.setInt(4, id_piatto);

            query.executeUpdate();
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
    public List<Piatto> findByName(String Nome_Piatto) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();

        try {
            String SQLQuery = "SELECT * FROM piatto WHERE Nome_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Piatto);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                piatti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }

    @Override
    public List<Piatto> findByIngediente(Ingrediente Ingrediente) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        String ingrediente = Ingrediente.getNome();

        try {
            String SQLQuery = "SELECT piatto.ID_Piatto, piatto.Nome_Piatto, piatto.Preparazione, piatto.CF, piatto.Deleted FROM chefskiss.piatto NATURAL JOIN chefskiss.contiene WHERE Ingrediente = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, ingrediente);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                piatti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }

    @Override
    public List<Piatto> findBySede(Sede Sede) {
        PreparedStatement query;
        List<Piatto> piatti = new ArrayList<>();
        String sede = Sede.getCoordinate();

        try {
            String SQLQuery = "SELECT piatto.ID_Piatto, piatto.Nome_Piatto, piatto.Preparazione, piatto.CF, piatto.Deleted FROM chefskiss.piatto NATURAL JOIN chefskiss.servito_in WHERE Sede = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, sede);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                piatti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

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
            String SQLQuery = "SELECT * FROM chefskiss.piatto WHERE CF = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, cf);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                piatti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

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
        // TODO: preparazione
        piatto.setCF_Utente(rs.getString("CF"));
        piatto.setDeleted(rs.getBoolean("Deleted"));

        return piatto;
    }
}