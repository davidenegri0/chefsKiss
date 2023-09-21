package com.project.chefskiss.modelObjects;

import java.util.HashMap;
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
    private Contiene[] Piatto_Ingrediente;
    public void setContieneP (Contiene[] piatti_ingredienti) { Piatto_Ingrediente = piatti_ingredienti; }
    public Contiene[] getContieneP() { return Piatto_Ingrediente; }
    public Contiene getContieneP(int index) {return this.Piatto_Ingrediente[index]; }
    public void setContieneP(int index, Contiene piatto_ingrediente) {
        this.Piatto_Ingrediente[index] = piatto_ingrediente;
    }


    /* [ piatto - sede (N:M) ] */
    private Sede[] Sedi;
    public Sede[] getSediP() { return Sedi; } // metodo per leggere tutte le sedi in cui è servito il piatto
    public void setSediP(Sede[] sedi) { this.Sedi = sedi; } // metodo per inserire tutte le sedi in cui è servito il piatto
    public Sede getSedeP(int index) {return this.Sedi[index]; }
    public void setSedeP (int index, Sede sede) {
        this.Sedi[index] = sede;
    }


    /* [ piatto - chef (1:1) ] e [ piatto - privato (1:1) ] */
    private User utente;
    public void setUtenteP( User utente ) { this.utente = utente; }
    public User getUtenteP() { return utente; }

    /* [ piatto - utente ] */
    private Recensione[] recensioni;
    public void setRecensioneP (Recensione[] recensioni) { recensioni = recensioni; }
    public Recensione[] getRecensioneP() { return recensioni; }
    public Recensione getRecensioneP(int index) {return this.recensioni[index]; }
    public void setRecensioneP(int index, Recensione recensione) {
        this.recensioni[index] = recensione;
    }
}