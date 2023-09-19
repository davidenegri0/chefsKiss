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

    // TODO: [ piatto - ingrediente (M:N) ]

    /* [ piatto - sede (N:M) ] */
    private Sede[] Sedi;
    public Sede[] getSedi() { return Sedi; } // metodo per leggere tutte le sedi in cui è servito il piatto
    public void setSedi(Sede[] sedi) { this.Sedi = sedi; } // metodo per inserire tutte le sedi in cui è servito il piatto
    // TODO: metodi per inserire e leggere una sede in un indice specifico --> da valutare se è utile


    /* [ piatto - chef (1:1) ] e [ piatto - privato (1:1) ] */
    private String CF_Utente;
    public void setCF_Utente( String cf ) { CF_Utente = cf; }
    public String getCF_Utente() { return CF_Utente; }

    // TODO: [ piatto - utente ] come ingredienti


    // TODO: piatto - ingrediente


    // TODO: piatto - utente
}