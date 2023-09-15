package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Ingrediente;

public interface IngredienteDAO {
    public Ingrediente create (
            String Nome_Ingrediente,
            String Gruppo_Allergenico
    );

    public void update (Ingrediente ingrediente);

    public void delete (Ingrediente ingrediente);

    // cerca un ingrediente per nome
    public Ingrediente findByName (String Nome_Ingrediente);

    // cerca un ingrediente per gruppo allergenico
    public Ingrediente[] findByGruppo (String Gruppo_Allergenico);
}
