package com.project.chefskiss.modelObjects;

import java.util.List;

public class Ingrediente {
    private String Nome;
    private String Gruppo_Allergenico;
    private List<Contiene> Piatto_Ingrediente;

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

    public void setContieneI (List<Contiene> piatti_ingredienti) { Piatto_Ingrediente = piatti_ingredienti; }
    public List<Contiene> getContieneI() { return Piatto_Ingrediente; }
    public Contiene getContieneI(int index) {return Piatto_Ingrediente.get(index); }
    public void setContieneI(int index, Contiene piatto_ingrediente) {
        Piatto_Ingrediente.set(index, piatto_ingrediente);
    }
}
