package com.project.chefskiss.modelObjects;

import java.sql.Date;

public class User {
    private String Nome;
    private String Congnome;
    private String Email;
    private String N_Telefono;
    private Date D_Nascita;
    private Date D_Iscrizione;

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setCongnome(String congnome) {
        Congnome = congnome;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setN_Telefono(String n_Telefono) {
        N_Telefono = n_Telefono;
    }

    public void setD_Iscrizione(Date d_Iscrizione) {
        D_Iscrizione = d_Iscrizione;
    }

    public void setD_Nascita(Date d_Nascita) {
        D_Nascita = d_Nascita;
    }

    public String getNome() {
        return Nome;
    }

    public String getCongnome() {
        return Congnome;
    }

    public String getEmail() {
        return Email;
    }

    public String getN_Telefono() {
        return N_Telefono;
    }

    public Date getD_Iscrizione() {
        return D_Iscrizione;
    }

    public Date getD_Nascita() {
        return D_Nascita;
    }
}
