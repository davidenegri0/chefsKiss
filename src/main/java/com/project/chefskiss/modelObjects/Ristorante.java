package com.project.chefskiss.modelObjects;

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
    private Sede[] Sedi;
    public void setSedeRi (Sede[] sedi) { this.Sedi = sedi; }
    public Sede[] getSedeRi() { return Sedi; }
    public Sede getSedeRi(int index) {return this.Sedi[index]; }
    public void setSedeRi(int index, Sede sede) {
        this.Sedi[index] = sede;
    }
}
