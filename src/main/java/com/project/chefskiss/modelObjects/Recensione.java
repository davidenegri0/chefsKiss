package com.project.chefskiss.modelObjects;

import java.sql.Clob;

public class Recensione {
    private Integer Voto;
    private String Commento;
    private Piatto Piatto;
    private User Utente;

    public Integer getVoto() { return Voto; }
    public void setVoto(Integer voto) { Voto = voto; }

    public String getCommento() { return Commento; }
    public void setCommento(String commento) { Commento = commento; }

    public Piatto getPiattoR(){ return Piatto; }
    public void setPiattoR(Piatto piatto) { this.Piatto = piatto; }

    public User getUtenteR() { return Utente; }
    public void setUtenteR(User utente) { Utente = utente; }

    //Funzione per ottenere l'immagine con le stelle dipendentemente dal voto dato
    public String getStarsRating(){
        String relPath = "/img/rating_stars/rating-star-icon-";
        String fileType = ".png";
        switch (this.Voto) {
            case 1 -> {
                return relPath+"1-of-5"+fileType;
            }
            case 2 -> {
                return relPath+"2-of-5"+fileType;
            }
            case 3 -> {
                return relPath+"3-of-5"+fileType;
            }
            case 4 -> {
                return relPath+"4-of-5"+fileType;
            }
            case 5 -> {
                return relPath+"5-of-5"+fileType;
            }
            default -> {
                System.out.println("Voto recensione non valida, " +
                        "supposta mancanza di votazione");
                return relPath+"3-of-5"+fileType;
            }
        }
    }
}
