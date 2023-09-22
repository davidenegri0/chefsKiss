package com.project.chefskiss.modelObjects;

import java.util.List;

public class Ristorante {
    private Integer ID_Ristorante;
    private String Nome;
    private Boolean Deleted;

    // GETTER e SETTER
    public void setID (Integer ID) { ID_Ristorante = ID; }
    public Integer getID_Ristorante () { return ID_Ristorante; }
    public void setNome (String nome) { Nome = nome; }
    public String getNome () { return Nome; }
    public void setDeleted (Boolean deleted) { Deleted = deleted; }
    public Boolean getDeleted () { return Deleted; }

    // RELAZIONI

    /* [ Ristorante - Utente (1:1) ] */
    private User Utente;
    public void setUtenteRi (User utente) { Utente = utente; }
    public User getUtenteRi () { return Utente; }

    /* [ Ristorante - sede (1:N) ] --> inserita in sede (dalla parte N) */
    private List<Sede> Sedi;
    public void setSedeRi (List<Sede> sedi) { this.Sedi = sedi; }
    public List<Sede> getSedeRi() { return Sedi; }
    public Sede getSedeRi(int index) {return Sedi.get(index); }
    public void setSedeRi(int index, Sede sede) {
        Sedi.set(index, sede);
    }
}
