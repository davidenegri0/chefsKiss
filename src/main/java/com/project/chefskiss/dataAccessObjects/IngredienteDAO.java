package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Ingrediente;

import java.util.List;

public interface IngredienteDAO {

    // cerca un ingrediente per nome
    public Ingrediente findByName (String Nome_Ingrediente);

    // cerca un ingrediente per gruppo allergenico
    //TODO: Valutare l'uso di questa
    //public List<Ingrediente> findByGruppo (String Gruppo_Allergenico);
}
