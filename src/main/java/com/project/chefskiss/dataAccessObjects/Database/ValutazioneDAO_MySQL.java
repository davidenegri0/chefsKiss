package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.configurations.Config;
import com.project.chefskiss.dataAccessObjects.ValutazioneDAO;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import com.project.chefskiss.modelObjects.Valutazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ValutazioneDAO_MySQL implements ValutazioneDAO {
    Connection conn;

    public ValutazioneDAO_MySQL (Connection conn) { this.conn = conn; }
    @Override
    public Valutazione create (User utente, Sede sede, Integer voto){
        PreparedStatement query;
        Valutazione valutazione = new Valutazione();

        try{
            String SQLQuery = "INSERT INTO valuta (CF, Coordinate, Voto) VALUES(?,?,?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, utente.getCF());
            query.setString(2, sede.getCoordinate());
            query.setInt(3, voto);

            query.executeUpdate();

            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        try{
            String SQLQuery = "SELECT COUNT(*) AS somma FROM valuta WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, utente.getCF());
            ResultSet result = query.executeQuery();

            if (result.next()){
                if (result.getInt("somma") > Config.MAX_VALUTAZIONI){
                    System.out.println(result.getInt("somma"));
                    String SQLQuery2 = "UPDATE utente SET Verificato = 1 WHERE CF = ?";

                    query = conn.prepareStatement(SQLQuery2);
                    query.setString(1, utente.getCF());
                    query.executeUpdate();
                }
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        valutazione.setUtenteV(utente);
        valutazione.setSedeV(sede);
        valutazione.setVoto(voto);

        return valutazione;
    }

    @Override
    public void update(Valutazione valutazione){
        PreparedStatement query;

        try{
            String SQLQuery = "UPDATE valuta SET Voto = ? WHERE Coordinate = ? AND CF = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, valutazione.getVoto());
            query.setString(2, valutazione.getSedeV().getCoordinate());
            query.setString(3, valutazione.getUtenteV().getCF());

            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete (Valutazione valutazione){
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM valuta WHERE CF = ? AND Coordinate = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setString(1, valutazione.getUtenteV().getCF());
            query.setString(2, valutazione.getSedeV().getCoordinate());

            query.executeUpdate();

            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteByCF (String CF){
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM valuta WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteBySede (String Coordinate){
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM valuta WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Coordinate);

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Valutazione> miglioriSedi (Integer numero){
        List<Valutazione> sedi = new ArrayList<>();
        PreparedStatement query;

        try{
            String SQLQuery = "SELECT *" +
                    "FROM (" +
                    "    SELECT Coordinate, AVG(voto) AS media_voto" +
                    "    FROM valuta" +
                    "    GROUP BY Coordinate" +
                    "    ORDER BY media_voto DESC" +
                    ") as sedi_migliori";

            query = conn.prepareStatement(SQLQuery);
            ResultSet result = query.executeQuery();

            for (int i = 0; result.next() && i<numero ; i++){
                sedi.add(read(result));
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return sedi;
    }

    @Override
    public boolean checkValutazione(String CF_recensore, String coordinate_sede){
        boolean check = false;

        PreparedStatement query;
        try{
            String SQLQuery = "SELECT * FROM valuta WHERE CF = ? AND Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF_recensore);
            query.setString(2, coordinate_sede);

            ResultSet result;
            result = query.executeQuery();

            if (result.next()) check = true;

            result.close();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return check;
    }

    @Override
    public Valutazione findByCF_Coordinate (String CF, String Coordinate){
        PreparedStatement query;
        Valutazione valutazione = new Valutazione();

        try{
            String SQLQuery = "SELECT * FROM valuta WHERE CF = ? AND Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);
            query.setString(2, Coordinate);

            ResultSet result = query.executeQuery();
            if (result.next())
                valutazione = read(result);

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return valutazione;
    }

    @Override
    public List<Valutazione> findBySede (Sede sede){
        PreparedStatement query;
        List<Valutazione> valutazioni = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM valuta WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, sede.getCoordinate());

            ResultSet result = query.executeQuery();

            while (result.next()){
                valutazioni.add(read(result));
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return valutazioni;
    }

    private Valutazione read (ResultSet rs) throws SQLException{
        Valutazione valutazione = new Valutazione();
        Sede sede = new Sede();
        User utente = new User();

        valutazione.setSedeV(sede);
        valutazione.setUtenteV(utente);

        valutazione.getUtenteV().setCF(rs.getString("CF"));
        valutazione.getSedeV().setCoordinate(rs.getString("Coordinate"));
        valutazione.setVoto(rs.getInt("Voto"));

        return valutazione;
    }
}
