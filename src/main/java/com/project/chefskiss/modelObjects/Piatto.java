package com.project.chefskiss.modelObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Piatto {
    private int Id_Piatto;
    private String Nome_Piatto;
    private String Preparazione;
    private Boolean Deleted;

    // GETTER - SETTER
    public void setID(Integer id_piatto) { Id_Piatto = id_piatto; }
    public void setNome(String nome_piatto) {
        Nome_Piatto = nome_piatto;
    }
    public void setPreparazione(String preparazione) {
        Preparazione = preparazione;
    }
    public void setDeleted(Boolean deleted) {
        Deleted = deleted;
    }
    public int getId (){
        return Id_Piatto;
    }
    public String getNome() {
        return Nome_Piatto;
    }
    public String getPreparazione() {
        return Preparazione;
    }
    public Boolean getDeleted() {
        return Deleted;
    }


    // RELAZIONI

    /* [ piatto - ingrediente (M:N) ] --> contiene */
    private List<Contiene> Piatto_Ingrediente;
    public void setContieneP (List<Contiene> piatti_ingredienti) { Piatto_Ingrediente = piatti_ingredienti; }
    public List<Contiene> getContieneP() { return Piatto_Ingrediente; }
    public Contiene getContieneP(int index) {return Piatto_Ingrediente.get(index); }
    public void setContieneP(int index, Contiene piatto_ingrediente) {
        Piatto_Ingrediente.set(index, piatto_ingrediente);
    }


    /* [ piatto - sede (N:M) ] */
    private List<Sede> Sedi;
    public List<Sede> getSediP() { return Sedi; } // metodo per leggere tutte le sedi in cui è servito il piatto
    public void setSediP(List<Sede> sedi) { this.Sedi = sedi; } // metodo per inserire tutte le sedi in cui è servito il piatto
    public Sede getSedeP(int index) {return Sedi.get(index); }
    public void setSedeP (int index, Sede sede) {
        Sedi.set(index, sede);
    }


    /* [ piatto - chef (1:1) ] e [ piatto - privato (1:1) ] */
    private User utente;
    public void setUtenteP( User utente ) { this.utente = utente; }
    public User getUtenteP() { return utente; }

    /* [ piatto - utente ] */
    private List<Recensione> recensioni;
    public void setRecensioneP (List<Recensione> recensioni) { this.recensioni = recensioni; }
    public List<Recensione> getRecensioneP() { return recensioni; }
    public Recensione getRecensioneP(int index) {return recensioni.get(index); }
    public void setRecensioneP(int index, Recensione recensione) {
        recensioni.set(index, recensione);
    }
}