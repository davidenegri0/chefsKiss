package com.project.chefskiss.dataAccessObjects.Database;

import com.project.chefskiss.dataAccessObjects.IngredienteDAO;
import com.project.chefskiss.modelObjects.Ingrediente;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDAO_MySQL implements IngredienteDAO {
    Connection conn;
    public IngredienteDAO_MySQL (Connection conn) { this.conn = conn; }

    @Override
    public Ingrediente findByName(String Nome_Ingrediente) {
        PreparedStatement query;
        Ingrediente ingrediente = new Ingrediente();

        try {
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ingrediente WHERE Nome_Ingrediente LIKE ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+Nome_Ingrediente+"%");

            ResultSet result = query.executeQuery();

            if (result.next()){
                ingrediente = read(result);
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ingrediente;
    }

    @Override
    public List<Ingrediente> getAllIngredients() {
        PreparedStatement query;
        List<Ingrediente> listaIngredienti = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ingrediente ORDER BY Nome_Ingrediente";

            query = conn.prepareStatement(SQLQuery);
            ResultSet result = query.executeQuery();

            while (result.next()){
                listaIngredienti.add(read(result));
            }

            System.out.println("Lettura dati ingredienti completata!");
            result.close();
            query.close();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return listaIngredienti;
    }

    @Override
    public List<String> getAllAllergeni(){
        PreparedStatement query;
        List<String> allergeni = new ArrayList<>();

        try{
            String SQLQuery = "SELECT DISTINCT Gruppo_Allergenico FROM boru5sh3eoe2vtbznsll.ingrediente";

            query = conn.prepareStatement(SQLQuery);

            ResultSet result = query.executeQuery();

            while (result.next()){
                allergeni.add(result.getString("Gruppo_Allergenico"));
            }

            result.close();
            query.close();

        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

        return allergeni;
    }

    /* SE TUTTO VA BENE, QUESTO NON SERVE
    @Override
    public List<Ingrediente> findByGruppo(String Gruppo_Allergenico) {
        PreparedStatement query;
        List<Ingrediente> ingredienti = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM boru5sh3eoe2vtbznsll.ingrediente WHERE Gruppo_Allergenico LIKE ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, "%"+Gruppo_Allergenico+"%");

            ResultSet result = query.executeQuery();

            if (result.next()) {
                ingredienti.add(read(result));
                System.out.println("Lettura dati completata!");
            }

            result.close();
            query.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ingredienti;
    }
     */

    private Ingrediente read(ResultSet rs) throws SQLException {
        Ingrediente ingrediente = new Ingrediente();

        ingrediente.setNome(rs.getString("Nome_Ingrediente"));
        ingrediente.setGruppo_Allergenico(rs.getString("Gruppo_Allergenico"));

        return ingrediente;
    }
}
