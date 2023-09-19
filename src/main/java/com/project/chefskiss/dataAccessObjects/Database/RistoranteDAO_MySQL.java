package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.modelObjects.Ristorante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RistoranteDAO_MySQL implements RistoranteDAO {
    Connection conn;

    public RistoranteDAO_MySQL (Connection conn) { this.conn = conn; }
    @Override
    public Ristorante create(String Nome_Ristorante, String CF_Ristoratore) {
        PreparedStatement query;
        Ristorante risto = new Ristorante();

        try {
            String SQLQuery = "INSERT INTO chefskiss.ristorante(Nome_Ristorante, CF) VALUES (?, ?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Ristorante);
            query.setString(2, CF_Ristoratore);

            query.executeUpdate();

            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return risto;
    }

    @Override
    public void update(Ristorante ristorante) {
        PreparedStatement query;
        Integer id_ristorante = ristorante.getID_Ristorante();

        try{
            String SQLQuery = "UPDATE chefskiss.ristorante " +
                    "SET Nome_Ristorante = ? " +
                    "CF = ? " +
                    "WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, ristorante.getNome());
            query.setString(2, ristorante.getCF_Ristoratore());
            query.setInt(3, ristorante.getID_Ristorante());

            query.executeUpdate();

            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Ristorante ristorante) {
        PreparedStatement query;

        try {
            String SQLQuery = "UPDATE chefskiss.ristorante SET Deleted = 'Y' WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, ristorante.getID_Ristorante());

            query.executeUpdate();

            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Ristorante findByName(Ristorante risto) {
        PreparedStatement query;
        Ristorante ristorante = new Ristorante();

        try {
            String SQLQuery = "SELECT * FROM chefskiss.ristorante WHERE Nome_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, risto.getNome());

            ResultSet result = query.executeQuery();

            if (result.next()) {
                ristorante = read (result);
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return ristorante;
    }

    @Override
    public Ristorante findByRistoratore(Ristorante risto) {
        PreparedStatement query;
        Ristorante ristorante = new Ristorante();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.ristorante WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, risto.getCF_Ristoratore());

            ResultSet result = query.executeQuery();

            if (result.next()) {
                ristorante = read (result);
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Ristorante read (ResultSet rs) throws SQLException {
        Ristorante ristorante = new Ristorante();

        ristorante.setID(rs.getInt("ID_Ristorante"));
        ristorante.setNome(rs.getString("Nome_Ristorante"));
        ristorante.setCF_Ristoratore(rs.getString("CF"));
        ristorante.setDeleted(rs.getBoolean("Deleted"));

        return ristorante;
    }
}
