package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.PrenotazioneDAO;
import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.*;

public class PrenotazioneDAO_MySQL implements PrenotazioneDAO {
    Connection conn;
    public PrenotazioneDAO_MySQL(Connection conn){ this.conn = conn; }
    @Override
    public Prenotazione create (User utente, Sede sede, Date data, Time orario, Integer n_posti) {
        Prenotazione prenotazione = new Prenotazione();
        PreparedStatement query;

        try{

            if ( verifica_posti_disponibili(sede.getCoordinate(), orario, data) >= n_posti ){
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
            else {
                throw new RuntimeException("Non ci sono abbastanza posti disponibili");
            }

        } catch (SQLException e){
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

        try{
            if ( (verifica_posti_disponibili(prenotazione.getSedeP().getCoordinate(), prenotazione.getOrario(), prenotazione.getData()) + prenotazione.getN_Posti()) >= prenotazione.getN_Posti() ){

                String SQLQuery = "UPDATE chefskiss.prenota_in SET Data = ?, Orario = ?, N_Posti = ? WHERE ID_Prenotazione = ?";

                query = conn.prepareStatement(SQLQuery);
                query.setDate(1, prenotazione.getData());
                query.setTime(2, prenotazione.getOrario());
                query.setInt(3, prenotazione.getN_Posti());
                query.setInt(4, prenotazione.getID());

                query.executeUpdate();
                query.close();
            }
            else {
                throw new RuntimeException("Cambio prenotazione non disponibile per esaurimento posti");
            }

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Prenotazione prenotazione) {
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM chefskiss.prenota_in WHERE ID_Prenotazione = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, prenotazione.getID());

            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Integer verifica_posti_disponibili (String coordinate, Time orario, Date data){
        PreparedStatement query2;
        PreparedStatement query3;

        try{
            String SQLQuery2 = "SELECT Posti_Disponibili FROM chefskiss.sede WHERE Coordinate = ?";
            query2 = conn.prepareStatement(SQLQuery2);
            query2.setString(1, coordinate);
            ResultSet result2 = query2.executeQuery();

            Integer posti_disponibili = result2.getInt("Posti_Disponibili");


            String SQLQuery3 = "SELECT SUM(N_Posti) AS somma_posti_prenotati " +
                    "FROM prenota_in " +
                    "WHERE Coordinate = ? AND Orario = ? AND Data = ?";
            query3 = conn.prepareStatement(SQLQuery3);
            query3.setString(1, coordinate);
            query3.setTime(2, orario);
            query3.setDate(3, data);
            ResultSet result3 = query3.executeQuery();

            Integer posti_occupati = result3.getInt("somma_posti_prenotati");

            return (posti_disponibili - posti_occupati);
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
