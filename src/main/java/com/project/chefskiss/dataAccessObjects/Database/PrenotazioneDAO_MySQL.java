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
            String SQLQuery = "INSERT INTO chefskiss.prenota_in (CF, Coordinate, Data, Orario, N_Posti) VALUES (?,?,?,?,?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, utente.getCF());
            query.setString(2, sede.getCoordinate());
            query.setDate(3, data);
            query.setTime(4, orario);
            query.setInt(5, n_posti); // TODO: da inserire controllo su posti disponibili

            query.executeUpdate();

            query.close();
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
            String SQLQuery = "UPDATE chefskiss.prenota_in SET Data = ?, Orario = ?, N_Posti = ? WHERE CF = ? AND Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setDate(1, prenotazione.getData());
            query.setTime(2, prenotazione.getOrario());
            query.setInt(3, prenotazione.getN_Posti()); // TODO: da inserire controllo su posti disponibili (caliamo i posti disponibili nella tabella sedi?)
            query.setString(4, prenotazione.getUtenteP().getCF() );
            query.setString(5, prenotazione.getSedeP().getCoordinate());

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Prenotazione prenotazione) {
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM chefskiss.prenota_in WHERE CF = ? AND Coordinate = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, prenotazione.getUtenteP().getCF() );
            query.setString(2, prenotazione.getSedeP().getCoordinate());

            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
