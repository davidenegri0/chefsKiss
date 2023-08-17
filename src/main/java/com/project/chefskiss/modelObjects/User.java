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
    private final Map<String, Boolean> Privileges = new HashMap<>(5);

    public void setCF(String CF) {
        this.CF = CF;
    }

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
        Privileges.put("Cliente", Se_Cliente);
        Privileges.put("Verificato", Verificato);
        Privileges.put("Privato", Se_Privato);
        Privileges.put("Chef", Se_Chef);
        Privileges.put("Ristoratore", Se_Ristoratore);
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

    public Boolean isCliente(){
        return Privileges.get("Cliente");
    }

    public Boolean isClienteVerificato(){
        if (this.isCliente())
        {
            return Privileges.get("Verificato");
        }
        else{
            Privileges.put("Verificato", false);
            return false;
        }
    }

    public Boolean isPrivato(){
        return Privileges.get("Privato");
    }

    public Boolean isChef(){
        return Privileges.get("Chef");
    }

    public Boolean isRistoratore(){
        return Privileges.get("Ristoratore");
    }
}
