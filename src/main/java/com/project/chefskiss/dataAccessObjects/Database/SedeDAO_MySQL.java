package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.SedeDAO;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SedeDAO_MySQL implements SedeDAO {
    Connection conn;
    public SedeDAO_MySQL (Connection conn){ this.conn = conn; }
    @Override
    public Sede create(String Coordinate, String Via, String Citta, Integer Posti_Disponibili, Integer ID_Ristorante) {
        PreparedStatement query;
        Sede sede = new Sede();
        sede.setCoordinate(Coordinate);
        sede.setVia(Via);
        sede.setCitta(Citta);
        sede.setPosti(Posti_Disponibili);
        sede.setID_Ristorante(ID_Ristorante);

        try{
            String SQLQuery = "INSERT INTO chefskiss.sede(Coordinate, Via, Citta, Posti_Disponibili, ID_Ristorante) " +
                    "VALUES (?, ?, ?, ?, ?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Coordinate);
            query.setString(2, Via);
            query.setString(3, Citta);
            query.setInt(4, Posti_Disponibili);
            query.setInt(5, ID_Ristorante);

            query.executeUpdate();

            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return sede;
    }

    @Override
    public void update(Sede sede) {
        PreparedStatement query;

        try {
            String SQLQuery = "UPDATE chefskiss.sede SET Via = ?, Citta = ?, Posti_Disponibili = ?, ID_Ristorante = ? WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, sede.getVia());
            query.setString(2, sede.getCitta());
            query.setInt(3, sede.getPosti());
            query.setInt(4, sede.getID_Ristorante());

            query.executeUpdate();

            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Sede sede) {
        PreparedStatement query;

        try{
            String SQLQuery = "UPDATE chefskiss.sede SET Deleted = 'Y' WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, sede.getCoordinate());

            query.executeUpdate();

            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Sede findByPosition(String Via, String Citta) {
        PreparedStatement query;
        Sede sede = new Sede();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.sede WHERE Via = ? AND Citta = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Via);
            query.setString(2, Citta);

            ResultSet result = query.executeQuery();

            if (result.next()) {
                sede = read(result);
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return sede;
    }

    @Override
    public List<Sede> findByRistorante(String nomeRistorante) {
        PreparedStatement query;
        List<Sede> sedi = new ArrayList<>();

        try{
            String SQLQuery = "SELECT Coordinate, Via, Citta, Posti_Disponibili, ID_Ristorante, Deleted FROM chefskiss.sede NATURAL JOIN chefskiss.ristorante WHERE ristorante.Nome_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, nomeRistorante);

            ResultSet result = query.executeQuery();

            while (result.next()){
                sedi.add(read(result));
            }

            System.out.println("Lettura dati completata!");
            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return sedi;
    }

    @Override
    public List<Sede> findByRistorante(Ristorante ristorante) {
        PreparedStatement query;
        List<Sede> sedi = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.sede WHERE ID_Ristorante = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, ristorante.getID_Ristorante());

            ResultSet result = query.executeQuery();

            while (result.next()){
                sedi.add(read(result));
            }

            System.out.println("Lettura dati completata!");
            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return sedi;
    }

    @Override
    public Sede findByCoordinate(String Coordinate) {
        PreparedStatement query;
        Sede sede = new Sede();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.sede WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Coordinate);

            ResultSet result = query.executeQuery();

            if(result.next()){
                sede = read(result);
                System.out.println("Lettura dato completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return sede;
    }

    private Sede read(ResultSet rs) throws SQLException {
        Sede sede = new Sede();

        sede.setCoordinate(rs.getString("Coordinate"));
        sede.setVia(rs.getString("Via"));
        sede.setCitta(rs.getString("Citta"));
        sede.setPosti(rs.getInt("Posti_Disponibili"));
        sede.setID_Ristorante(rs.getInt("ID_Ristorante"));
        sede.setDeleted(rs.getBoolean("Deleted"));

        return sede;
    }
}
