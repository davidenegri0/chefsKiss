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
            String SQLQuery = "SELECT * FROM chefskiss.ingrediente WHERE Nome_Ingrediente = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Nome_Ingrediente);

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
    public List<Ingrediente> findByGruppo(String Gruppo_Allergenico) {
        PreparedStatement query;
        List<Ingrediente> ingredienti = new ArrayList<>();

        try{
            String SQLQuery = "SELECT * FROM chefskiss.ingrediente WHERE Gruppo_Allergenico = ?";

            query = conn.prepareStatement(SQLQuery);
            query.setString(1, Gruppo_Allergenico);

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

    private Ingrediente read(ResultSet rs) throws SQLException {
        Ingrediente ingrediente = new Ingrediente();

        ingrediente.setNome(rs.getString("Nome_Ingrediente"));
        ingrediente.setGruppo_Allergenico(rs.getString("Gruppo_Allergenico"));

        return ingrediente;
    }
}
