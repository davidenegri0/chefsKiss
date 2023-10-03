package com.project.chefskiss.modelObjects;

public class Valutazione {
    private Integer Voto;
    private User Utente;
    private Sede Sede;

    public Integer getVoto() { return Voto; }
    public void setVoto(Integer voto) { Voto = voto; }

    public User getUtenteV() { return Utente; }
    public void setUtenteV(User utente) { Utente = utente; }

    public Sede getSedeV() { return Sede; }
    public void setSedeV(Sede sede) { Sede = sede; }

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
                System.out.println("Voto sede non valido, " +
                        "supposta mancanza di votazione");
                return relPath+"3-of-5"+fileType;
            }
        }
    }
}
