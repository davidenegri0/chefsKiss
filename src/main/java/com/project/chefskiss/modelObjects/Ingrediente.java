package com.project.chefskiss.modelObjects;

public class Ingrediente {
    private String Nome;
    private String Gruppo_Allergenico;

    public void setNome (String nome) {
        Nome = nome;
    }
    public String getNome () {
        return Nome;
    }
    public void setGruppo_Allergenico (String gruppo_allergenico) {
        Gruppo_Allergenico = gruppo_allergenico;
    }
    public String getGruppo_Allergenico () {
        return Gruppo_Allergenico;
    }
}
