package com.project.chefskiss.modelObjects;

import java.sql.Clob;

public class Recensione {
    private Integer Voto;
    private Clob Commento;
    private Piatto Piatto;
    private User Utente;

    public Integer getVoto() { return Voto; }
    public void setVoto(Integer voto) { Voto = voto; }

    public Clob getCommento() { return Commento; }
    public void setCommento(Clob commento) { Commento = commento; }

    public Piatto getPiattoR(){ return Piatto; }
    public void setPiattoR(Piatto piatto) { this.Piatto = piatto; }

    public User getUtenteR() { return Utente; }
    public void setUtenteR(User utente) { Utente = utente; }
}
