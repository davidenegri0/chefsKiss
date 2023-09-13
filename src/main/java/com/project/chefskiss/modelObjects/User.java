package com.project.chefskiss.modelObjects;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String CF;
    private String Nome;
    private String Cognome;
    private String Email;
    private String Password;
    private String N_Telefono;
    private Date D_Nascita;
    private Date D_Iscrizione;
    private final Map<String, Boolean> Privileges = new HashMap<>(5);
    private String Username;
    private Boolean Deleted;

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

    public void setPassword(String password) {
        Password = password;
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
    public void setUsername (String username) {
        if (this.isPrivato()) {
            Username = username;
        }
        // else { Username = null; }
    }

    //TODO: set foto per utente privato
    //TODO: set di foto e CV per chef

    public void setDeleted(boolean deleted) { Deleted = deleted; }

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

    public String getPassword() { return Password; }

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

    public String getUsername(){
        if (this.isPrivato()){
            return Username;
        }
        else { return null; }
    }

    public Boolean isChef(){
        return Privileges.get("Chef");
    }

    public Boolean isRistoratore(){
        return Privileges.get("Ristoratore");
    }
    public Boolean isDeleted() { return Deleted; }

    public User setTotalData(String Nome, String Cognome, String CF, String Email, String Telefono, Date Data, String Password){
        this.setNome(Nome);
        this.setCognome(Cognome);
        this.setCF(CF);
        this.setEmail(Email);
        this.setN_Telefono(Telefono);
        this.setD_Nascita(Data);
        this.setPassword(Password);

        return this;
    }
}
