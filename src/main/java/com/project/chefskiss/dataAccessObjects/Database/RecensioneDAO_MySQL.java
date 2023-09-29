package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.RecensioneDAO;
import com.project.chefskiss.modelObjects.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO_MySQL implements RecensioneDAO {
    Connection conn;
    public RecensioneDAO_MySQL (Connection conn) {this.conn = conn; }
    @Override
    public Recensione create(User utente, Piatto piatto, Integer voto, String commento) {
        PreparedStatement query;
        Recensione recensione = new Recensione();

        try{
            String SQLQuery = "INSERT INTO chefskiss.recensisce(CF, ID_Piatto, Voto, Commento) VALUES (?,?,?,?)";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, utente.getCF());
            query.setInt(2, piatto.getId());
            query.setInt(3, voto);
            query.setString(4, commento);

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        recensione.setUtenteR(utente);
        recensione.setPiattoR(piatto);
        recensione.setVoto(voto);
        recensione.setCommento(commento);

        return recensione;
    }

    @Override
    public void update(Recensione recensione) {
        PreparedStatement query;

        try{
            String SQLQuery = "UPDATE chefskiss.recensisce SET Voto = ?, Commento = ? WHERE CF = ? AND ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, recensione.getVoto());
            query.setString(2, recensione.getCommento());
            query.setString(3, recensione.getUtenteR().getCF());
            query.setInt(4, recensione.getPiattoR().getId());

            query.executeUpdate();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Recensione recensione) {
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM chefskiss.recensisce WHERE CF = ? AND ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, recensione.getUtenteR().getCF());
            query.setInt(2, recensione.getPiattoR().getId());

            query.executeUpdate();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Recensione> findByPiatto (Integer id_piatto){
        List<Recensione> recensioni = new ArrayList<>();

        PreparedStatement query;

        try{
            String SQLQuery = "SELECT * FROM chefskiss.recensisce WHERE ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, id_piatto);
            ResultSet result = query.executeQuery();

            while (result.next()){
                recensioni.add(read(result));
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return recensioni;
    }

    @Override
    public Recensione findByPiatto_Utente (Integer id_piatto, String CF_user){
        Recensione recensioni = new Recensione();

        PreparedStatement query;

        try{
            String SQLQuery = "SELECT * FROM chefskiss.recensisce WHERE ID_Piatto = ? AND CF = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, id_piatto);
            query.setString(2, CF_user);
            ResultSet result = query.executeQuery();

            if (result.next()){
                recensioni = read(result);
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return recensioni;
    }

    public List<Recensione> miglioriPiatti (Integer numero){
        List<Recensione> piatti = new ArrayList<>();

        PreparedStatement query;

        try{
            String SQLQuery = "SELECT *" +
                    "FROM (" +
                    "    SELECT ID_Piatto, AVG(voto) AS media_voto" +
                    "    FROM chefskiss.recensisce" +
                    "    GROUP BY ID_Piatto" +
                    "    ORDER BY media_voto DESC" +
                    ") as piatti_migliori";

            query = conn.prepareStatement(SQLQuery);
            ResultSet result = query.executeQuery();

            for (int i = 0; result.next() && i<numero ; i++){
                piatti.add(read(result));
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }
    @Override
    public boolean checkRecensione(String CF_recensore, Integer id_piatto){
        boolean check = false;

        PreparedStatement query;
        try{
            String SQLQuery = "SELECT * FROM chefskiss.recensisce WHERE CF = ? AND ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, CF_recensore);
            query.setInt(2, id_piatto);

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

    private Recensione read (ResultSet rs) throws SQLException{
        Recensione recensione = new Recensione();
        Piatto piatto = new Piatto();
        User utente = new User();

        recensione.setUtenteR(utente);
        recensione.setPiattoR(piatto);

        recensione.getUtenteR().setCF(rs.getString("CF"));
        recensione.getPiattoR().setID(rs.getInt("ID_Piatto"));
        recensione.setVoto(rs.getInt("Voto"));
        recensione.setCommento(rs.getString("Commento"));

        return recensione;
    }
}
