package com.project.chefskiss.modelObjects;

import java.util.List;

public class Sede {
    private String Coordinate;
    private String Via;
    private String Citta;
    private Integer Posti;
    private Integer ID_Ristorante;
    private Boolean Deleted;
    private Float VotoMedio;

    // GETTER E SETTER
    public void setCoordinate (String coordinate) { Coordinate = coordinate;}
    public String getCoordinate () { return Coordinate; }
    public void setVia (String via) { Via = via; }
    public String getVia () { return Via; }
    public void setCitta (String citta) { Citta = citta; }
    public String getCitta () { return Citta; }
    public void setPosti (Integer numeroP) { Posti = numeroP; }
    public Integer getPosti () { return Posti; }
    public void setDeleted (boolean deleted) { Deleted = deleted; }
    public Boolean getDeleted () { return Deleted; }
    public void setID_Ristorante (Integer id_ristorante) { ID_Ristorante = id_ristorante; }
    public Integer getID_Ristorante () { return ID_Ristorante; }
    public void setVotoMedio (Float votoMedio) {
        VotoMedio = votoMedio;
    }
    public Float getVotoMedio () { return VotoMedio; }

    // RELAZIONI

    /* [sede - utente (N:M)] --> chef (lavora) */

    private List<User> Utenti;
    public void setUtenteS (List<User> utenti) { this.Utenti = utenti; }
    public List<User> getUtenteS() { return Utenti; }
    public User getUtenteS(int index) {return this.Utenti.get(index); }
    public void setUtenteS(int index, User utente) {
        this.Utenti.set(index, utente);
    }

    /* [sede - cliente (N:M)] --> prenotazione */
    private List<Prenotazione> Prenotazioni;
    public void setPrenotazioneS (List<Prenotazione> prenotazioni) { this.Prenotazioni = prenotazioni; }
    public List<Prenotazione> getPrenotazioneS() { return Prenotazioni; }
    public Prenotazione getPrenotazioneS(int index) {return this.Prenotazioni.get(index); }
    public void setPrenotazioneS (int index, Prenotazione prenotazione) {
        this.Prenotazioni.set(index, prenotazione);
    }

    /* [sede - cliente (N:M)] --> valutazione  */
    private List<Valutazione> Valutazioni;
    public void setValutazioneS (List<Valutazione> valutazioni) { this.Valutazioni = valutazioni; }
    public List<Valutazione> getValutazioneS() { return Valutazioni; }
    public Valutazione getValutazioneS(int index) {return this.Valutazioni.get(index); }
    public void setValutazioneS (int index, Valutazione valutazione) {
        this.Valutazioni.set(index, valutazione);
    }

    /* [sede - piatto (N:M)] */
    private List<Piatto> Piatti;
    public void setPiattoS (List<Piatto> piatti) { this.Piatti = piatti; }
    public List<Piatto> getPiattoS() { return Piatti; }
    public Piatto getPiattoS(int index) {return this.Piatti.get(index); }
    public void setPiattoS (int index, Piatto piatto) {
        this.Piatti.set(index,piatto);
    }

    /* [sede - ristorante (N:1)] */
    private Ristorante ristorante;
    public void setRistoranteS (Ristorante ristorante) {this.ristorante = ristorante; }
    public Ristorante getRistoranteS() { return ristorante; }

    //Funzione per ottenere l'immagine con le stelle dipendentemente della valutazione media
    public String getStarsRating(){
        int avgVoto = Math.round(getVotoMedio());
        String relPath = "/img/rating_stars/rating-star-icon-";
        String fileType = ".png";
        switch (avgVoto) {
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
                System.out.println("Media recensioni non valida, " +
                        "supposta mancanza di recensioni, invio rating medio");
                return relPath+"unknown"+fileType;
            }
        }
    }
}
