package com.project.chefskiss.modelObjects;

public class Ristorante {
    private Integer ID_Ristorante;
    private String Nome;
    private Boolean Deleted;

    // GETTER e SETTER
    public Integer getID_Ristorante () { return ID_Ristorante; }
    public void setNome (String nome) { Nome = nome; }
    public String getNome () { return Nome; }
    public void setDeleted (Boolean deleted) { Deleted = deleted; }
    public Boolean getDeleted () { return Deleted; }

    // RELAZIONI

    /* [ Ristorante - Utente (1:1) ] */
    private String CF_Ristoratore;
    public void setCF_Ristoratore (String cf) { CF_Ristoratore = cf; }
    public String getCF_Ristoratore () { return CF_Ristoratore; }

    /* [ Ristorante - sede (1:N) ] --> inserita in sede (dalla parte N) */
}
