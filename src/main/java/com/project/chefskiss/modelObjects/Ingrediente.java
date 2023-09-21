package com.project.chefskiss.modelObjects;

public class Ingrediente {
    private String Nome;
    private String Gruppo_Allergenico;
    private Contiene[] Piatto_Ingrediente;

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

    public void setContieneI (Contiene[] piatti_ingredienti) { Piatto_Ingrediente = piatti_ingredienti; }
    public Contiene[] getContieneI() { return Piatto_Ingrediente; }
    public Contiene getContieneI(int index) {return this.Piatto_Ingrediente[index]; }
    public void setContieneI(int index, Contiene piatto_ingrediente) {
        this.Piatto_Ingrediente[index] = piatto_ingrediente;
    }
}
