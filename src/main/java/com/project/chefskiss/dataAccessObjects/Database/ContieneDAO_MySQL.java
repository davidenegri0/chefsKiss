package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.ContieneDAO;
import com.project.chefskiss.modelObjects.Contiene;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContieneDAO_MySQL implements ContieneDAO {
    Connection conn;
    public ContieneDAO_MySQL (Connection conn) { this.conn = conn; }
    @Override
    public Contiene create(Piatto piatto, Ingrediente ingrediente, Integer quantita) {
        PreparedStatement query;
        Contiene contiene = new Contiene();

        try{
            String SQLQuery = "INSERT INTO chefskiss.contiene (ID_Piatto, Nome_Ingrediente, Quantita) VALUES (?,?,?)";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, piatto.getId());
            query.setString(2, ingrediente.getNome());
            query.setInt(3, quantita);

            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        contiene.setPiattoC(piatto);
        contiene.setIngredienteC(ingrediente);
        contiene.setQuantita(quantita);

        return contiene;
    }

    @Override
    public void update(Contiene contiene) {
        PreparedStatement query;

        try{
            String SQLQuery = "UPDATE chefskiss.contiene SET Quantita = ? WHERE ID_Piatto = ? AND Nome_Ingrediente = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, contiene.getQuantita());
            query.setInt(2, contiene.getPiattoC().getId());
            query.setString(3, contiene.getIngredienteC().getNome());

            query.executeUpdate();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Contiene contiene) {
        PreparedStatement query;

        try{
            String SQLQuery = "DELETE FROM chefskiss.contiene WHERE ID_Piatto = ? AND Nome_Ingrediente = ?";
            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, contiene.getPiattoC().getId());
            query.setString(2, contiene.getIngredienteC().getNome());

            query.executeUpdate();

            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Contiene> findByPiatto (Piatto piatto){
        PreparedStatement query;
        List<Contiene> piatti = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.contiene WHERE ID_Piatto = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, piatto.getId());

            ResultSet result = query.executeQuery();

            while (result.next()){
                piatti.add(read(result));
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return piatti;
    }
    public Contiene findByPiatto_Ingrediente (Piatto piatto, Ingrediente ingrediente){
        PreparedStatement query;
        Contiene quantita = new Contiene();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.contiene WHERE ID_Piatto = ? AND Nome_Ingrediente = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setInt(1, piatto.getId());
            query.setString(2, ingrediente.getNome());

            ResultSet result = query.executeQuery();

            if (result.next()){
                quantita = read(result);
            }

            result.close();
            query.close();

        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return quantita;
    }

    private Contiene read (ResultSet rs) throws SQLException{
        Contiene contiene = new Contiene();
        Piatto piatto = new Piatto();
        Ingrediente ingrediente = new Ingrediente();

        contiene.setPiattoC(piatto);
        contiene.setIngredienteC(ingrediente);

        contiene.getPiattoC().setID(rs.getInt("ID_Piatto"));
        contiene.getIngredienteC().setNome(rs.getString("Nome_Ingrediente"));
        contiene.setQuantita(rs.getInt("Quantita"));

        return contiene;
    }

}
