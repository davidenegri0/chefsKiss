package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO_MySQL implements PrenotazioneDAO {
    Connection conn;

    public PrenotazioneDAO_MySQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Prenotazione create(User utente, Sede sede, Date data, Time orario, Integer n_posti) {
        Prenotazione prenotazione = new Prenotazione();
        PreparedStatement query;

        try {

            if (verifica_posti_disponibili(sede.getCoordinate(), orario, data) >= n_posti && isPrenotazioneUp(utente.getCF(), data, orario)) {
                String SQLQuery = "INSERT INTO chefskiss.prenota_in (CF, Coordinate, Data, Orario, N_Posti) VALUES (?,?,?,?,?)";

                query = conn.prepareStatement(SQLQuery);
                query.setString(1, utente.getCF());
                query.setString(2, sede.getCoordinate());
                query.setDate(3, data);
                query.setTime(4, orario);
                query.setInt(5, n_posti);

                query.executeUpdate();
                query.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        prenotazione.setUtenteP(utente);
        prenotazione.setSedeP(sede);
        prenotazione.setData(data);
        prenotazione.setOrario(orario);
        prenotazione.setN_Posti(n_posti);

        return prenotazione;
    }

    @Override
    public void update(Prenotazione prenotazione) {
        PreparedStatement query;

        try {
            if ((verifica_posti_disponibili(prenotazione.getSedeP().getCoordinate(), prenotazione.getOrario(), prenotazione.getData()) + prenotazione.getN_Posti()) >= prenotazione.getN_Posti() && isPrenotazioneUp(prenotazione.getUtenteP().getCF(), prenotazione.getData(), prenotazione.getOrario())) {

                String SQLQuery = "UPDATE chefskiss.prenota_in SET Data = ?, Orario = ?, N_Posti = ? WHERE ID_Prenotazione = ?";

                query = conn.prepareStatement(SQLQuery);
                query.setDate(1, prenotazione.getData());
                query.setTime(2, prenotazione.getOrario());
                query.setInt(3, prenotazione.getN_Posti());
                query.setInt(4, prenotazione.getID());

                query.executeUpdate();
                query.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Prenotazione prenotazione) {
        PreparedStatement query;

        try {
            String SQLQuery = "DELETE FROM chefskiss.prenota_in WHERE ID_Prenotazione = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, prenotazione.getID());

            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteByCF (String CF){
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM chefskiss.prenota_in WHERE CF = ?";

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
            String SQLQuery = "DELETE FROM chefskiss.valuta WHERE Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Coordinate);

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer verifica_posti_disponibili(String coordinate, Time orario, Date data) {
        PreparedStatement query2;
        PreparedStatement query3;

        try {
            String SQLQuery2 = "SELECT Posti_Disponibili FROM chefskiss.sede WHERE Coordinate = ?";
            query2 = conn.prepareStatement(SQLQuery2);
            query2.setString(1, coordinate);
            ResultSet result2 = query2.executeQuery();
            Integer posti_disponibili=0;

            String SQLQuery3 = "SELECT SUM(N_Posti) AS somma_posti_prenotati " +
                    "FROM prenota_in " +
                    "WHERE Coordinate = ? AND Orario = ? AND Data = ?";
            query3 = conn.prepareStatement(SQLQuery3);
            query3.setString(1, coordinate);
            query3.setTime(2, orario);
            query3.setDate(3, data);
            ResultSet result3 = query3.executeQuery();

            Integer posti_occupati=0;
            if (result3.next() && result2.next()){
                posti_disponibili = result2.getInt("Posti_Disponibili");
                posti_occupati = result3.getInt("somma_posti_prenotati");
            }

            return (posti_disponibili - posti_occupati);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isPrenotazioneUp(String CF, Date data, Time orario) {
        boolean check = true;
        PreparedStatement query;

        try {
            String SQLQuery = "SELECT * FROM chefskiss.prenota_in WHERE Data = ? AND Orario = ? AND CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setDate(1, data);
            query.setTime(2, orario);
            query.setString(3, CF);

            ResultSet result;
            result = query.executeQuery();
            if (result.next()) check = false;

            result.close();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return check;
    }

    @Override
    public Prenotazione findById(Integer id){
        PreparedStatement query;
        Prenotazione prenotazione = new Prenotazione();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.prenota_in WHERE ID_Prenotazione = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, id);

            ResultSet result = query.executeQuery();

            if ( result.next() ) prenotazione = read(result);

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return prenotazione;
    }

    public List<Prenotazione> findByUser (String CF){
        List<Prenotazione> prenotazioni = new ArrayList<>();
        PreparedStatement query;

        try{
            String SQLQuery = "SELECT * FROM chefskiss.prenota_in NATURAL JOIN chefskiss.sede WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            ResultSet result = query.executeQuery();

            while (result.next()){
                prenotazioni.add(read_P_S(result));
            }

            result.close();
            query.close();

        }catch(SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return prenotazioni;
    }

    public boolean checkPrenotazione(String CF){
        boolean check = false;
        Prenotazione prenotazione;
        PreparedStatement query;
        Date dataSQLAttuale = new Date(System.currentTimeMillis());

        try{
            String SQLQuery = "SELECT * FROM chefskiss.prenota_in WHERE CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF);

            ResultSet result = query.executeQuery();
            while (result.next()){
                prenotazione = read(result);
                if (prenotazione.getData().before(dataSQLAttuale)) {
                    System.out.println("La data della valutazione è  successiva ad una prenotazione --> ok!");
                    check = true;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return check;
    }

    private Prenotazione read(ResultSet rs) throws SQLException{
        Prenotazione prenotazione = new Prenotazione();
        Sede sede = new Sede();
        User utente = new User();

        prenotazione.setSedeP(sede);
        prenotazione.setUtenteP(utente);

        prenotazione.setId(rs.getInt("ID_Prenotazione"));
        prenotazione.getUtenteP().setCF(rs.getString("CF"));
        prenotazione.getSedeP().setCoordinate(rs.getString("Coordinate"));
        prenotazione.setData(rs.getDate("Data"));
        prenotazione.setOrario(rs.getTime("Orario"));
        prenotazione.setN_Posti(rs.getInt("N_Posti"));

        return prenotazione;
    }

    private Prenotazione read_P_S(ResultSet rs) throws SQLException{
        Prenotazione prenotazione = new Prenotazione();
        Sede sede = new Sede();
        User utente = new User();

        prenotazione.setUtenteP(utente);
        prenotazione.setSedeP(sede);

        prenotazione.setId(rs.getInt("ID_Prenotazione"));
        prenotazione.getUtenteP().setCF(rs.getString("CF"));
        prenotazione.getSedeP().setCoordinate(rs.getString("Coordinate"));
        prenotazione.setData(rs.getDate("Data"));
        prenotazione.setOrario(rs.getTime("Orario"));
        prenotazione.setN_Posti(rs.getInt("N_Posti"));

        prenotazione.getSedeP().setVia(rs.getString("Via"));
        prenotazione.getSedeP().setCitta(rs.getString("Citta"));
        prenotazione.getSedeP().setPosti(rs.getInt("Posti_Disponibili"));

        return prenotazione;
    }
}
