package com.project.chefskiss.modelObjects;

import org.apache.tomcat.util.buf.Utf8Encoder;

public class Sede {
    private String Coordinate;
    private String Via;
    private String Citta;
    private Integer Posti;
    private Integer ID_Ristorante;
    private Boolean Deleted;

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

    /* [sede - piatto (N:M)] */
    private Integer[] piatti;
    public void setPiatti(Integer[] piatti) { this.piatti = piatti; }
    public Integer[] getPiatti () { return piatti; }

    /* TODO: [sede - cliente (N:M)] --> problema di ingrediente */
    /* TODO: [sede - cliente (N:M)] --> problema di ingrediente */

    /* [sede - ristorante (N:1)] */
    private Ristorante ristorante;
    public void setRistorante (Ristorante ristorante) {this.ristorante = ristorante; }
    public Ristorante getRistorante() { return ristorante; }
}
