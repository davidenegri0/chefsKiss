package com.project.chefskiss.modelObjects;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String CF;
    private String Nome;
    private String Cognome;
    private String Email;
    private String N_Telefono;
    private Date D_Nascita;
    private Date D_Iscrizione;
    private final Map<String, Boolean> Privileges = new HashMap<>();

    public void setNome(String nome) {
        Nome = nome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
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

    public void setPrivileges(Boolean Se_Cliente, Boolean Verificato, Boolean Se_Privato, Boolean Se_Chef, Boolean Se_Ristoratore) {
        Privileges.putIfAbsent("Cliente", Se_Cliente);
        Privileges.putIfAbsent("Verificato", Verificato);
        Privileges.putIfAbsent("Privato", Se_Privato);
        Privileges.putIfAbsent("Chef", Se_Chef);
        Privileges.putIfAbsent("Ristoratore", Se_Ristoratore);
    }

    public String getCF() {
        return CF;
    }

    public String getNome() {
        return Nome;
    }

    public String getCognome() {
        return Cognome;
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

    public String getPrivileges() {
        return "Se_Cliente="+Privileges.get("Cliente")+";"+
               "Verificato="+Privileges.get("Verificato")+";"+
               "Se_Privato="+Privileges.get("Privato")+";"+
               "Se_Chef="+Privileges.get("Chef")+";"+
               "Se_Ristoratore="+Privileges.get("Ristoratore");
    }
}
