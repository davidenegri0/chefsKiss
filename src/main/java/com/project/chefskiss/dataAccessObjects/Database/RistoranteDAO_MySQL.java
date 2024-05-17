package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.RistoranteDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RistoranteDAO_MySQL implements RistoranteDAO {
    Connection conn;

    public RistoranteDAO_MySQL (Connection conn) { this.conn = conn; }
    @Override
    public Ristorante create(String Nome_Ristorante, String CF_Ristoratore) {
        PreparedStatement query;
        Ristorante risto = new Ristorante();
        risto.setNome(Nome_Ristorante);
        User utente = new User();
        utente.setCF(CF_Ristoratore);
        risto.setUtenteRi(utente);

        try {
            String SQLQuery = "INSERT INTO boru5sh3eoe2vtbznsll.ristorante(Nome_Ristorante, CF, Deleted) VALUES (?, ?, ?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Ristorante);
            query.setString(2, CF_Ristoratore);
            query.setString(3, "N");

            query.executeUpdate();

            query.close();

            SQLQuery =
                    "SELECT ID_Ristorante " +
                    "FROM boru5sh3eoe2vtbznsll.ristorante " +
                    "WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF_Ristoratore);
            ResultSet result = query.executeQuery();
            if (result.next()) risto.setID(result.getInt("ID_Ristorante"));
            result.close();
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
            String SQLQuery = "UPDATE boru5sh3eoe2vtbznsll.ristorante " +
                    "SET Nome_Ristorante = ?, " +
                    "CF = ? " +
                    "WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, ristorante.getNome());
            query.setString(2, ristorante.getUtenteRi().getCF());
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
            String SQLQuery = "UPDATE boru5sh3eoe2vtbznsll.ristorante SET Deleted = 'Y' WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, ristorante.getID_Ristorante());

            query.executeUpdate();

            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Ristorante findById(Integer id) {
        PreparedStatement query;
        Ristorante ristorante = new Ristorante();

        try {
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ristorante WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, id);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                ristorante = read(result);
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
    public List<Ristorante> findByName(String risto) {
        PreparedStatement query;
        List<Ristorante> ristoranti = new ArrayList<>();

        try {
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ristorante WHERE Nome_Ristorante LIKE ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+risto+"%");

            ResultSet result = query.executeQuery();

            while (result.next()) {
                ristoranti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return ristoranti;
    }

    @Override
    public Ristorante findByRistoratore(String CF_Risto) {
        PreparedStatement query;
        Ristorante ristorante = new Ristorante();

        try{
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ristorante WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF_Risto);

            ResultSet result = query.executeQuery();

            if(result.next()) {
                ristorante = read (result);
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ristorante;
    }

    @Override
    public List<Ristorante> getAll() {
        PreparedStatement quary;
        List<Ristorante> ristoranti = new ArrayList<>();

        try {
            String SQLQuary = "SELECT * FROM boru5sh3eoe2vtbznsll.ristorante";

            quary = conn.prepareStatement(SQLQuary);

            ResultSet result = quary.executeQuery();

            while (result.next()){
                ristoranti.add(read(result));
            }

            System.out.println("Lettura dati completata!");

            quary.close();
            result.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ristoranti;
    }

    private Ristorante read (ResultSet rs) throws SQLException {
        Ristorante ristorante = new Ristorante();

        ristorante.setID(rs.getInt("ID_Ristorante"));
        ristorante.setNome(rs.getString("Nome_Ristorante"));

        User utente = new User();
        utente.setCF(rs.getString("CF"));

        ristorante.setUtenteRi(utente);
        ristorante.setDeleted(rs.getBoolean("Deleted"));

        return ristorante;
    }
}
