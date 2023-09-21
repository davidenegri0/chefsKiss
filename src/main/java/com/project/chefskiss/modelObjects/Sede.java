package com.project.chefskiss.modelObjects;

import org.apache.tomcat.util.buf.Utf8Encoder;

public class Sede {
    private String Coordinate;
    private String Via;
    private String Citta;
    private Integer Posti;
    private Integer ID_Ristorante;
    private Boolean Deleted;
    private User[] Utenti;
    private Prenotazione[] Prenotazioni;
    private Valutazione[] Valutazioni;
    private Piatto[] Piatti;
    private Ristorante ristorante;

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


    // RELAZIONI

    /* [sede - utente (N:M)] --> chef (lavora) */
    public void setUtenteS (User[] utenti) { this.Utenti = utenti; }
    public User[] getUtenteS() { return Utenti; }
    public User getUtenteS(int index) {return this.Utenti[index]; }
    public void setUtenteS(int index, User utente) {
        this.Utenti[index] = utente;
    }

    /* [sede - cliente (N:M)] --> prenotazione */
    public void setPrenotazioneS (Prenotazione[] prenotazioni) { this.Prenotazioni = prenotazioni; }
    public Prenotazione[] getPrenotazioneS() { return Prenotazioni; }
    public Prenotazione getPrenotazioneS(int index) {return this.Prenotazioni[index]; }
    public void setPrenotazioneS (int index, Prenotazione prenotazione) {
        this.Prenotazioni[index] = prenotazione;
    }

    /* [sede - cliente (N:M)] --> valutazione  */
    public void setValutazioneS (Valutazione[] valutazioni) { this.Valutazioni = valutazioni; }
    public Valutazione[] getValutazioneS() { return Valutazioni; }
    public Valutazione getValutazioneS(int index) {return this.Valutazioni[index]; }
    public void setValutazioneS (int index, Valutazione valutazione) {
        this.Valutazioni[index] = valutazione;
    }

    /* [sede - piatto (N:M)] */
    public void setPiattoS (Piatto[] piatti) { this.Piatti = piatti; }
    public Piatto[] getPiattoS() { return Piatti; }
    public Piatto getPiattoS(int index) {return this.Piatti[index]; }
    public void setPiattoS (int index, Piatto piatto) {
        this.Piatti[index] = piatto;
    }

    /* [sede - ristorante (N:1)] */
    public void setRistoranteS (Ristorante ristorante) {this.ristorante = ristorante; }
    public Ristorante getRistoranteS() { return ristorante; }
}
