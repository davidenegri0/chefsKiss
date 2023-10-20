package com.project.chefskiss.dataAccessObjects;

import java.sql.Date;
import java.util.List;

import com.project.chefskiss.Exceptions.UserAlreadyKnownException;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
public interface UserDAO {
    public User create (
            String CF,
            String Nome,
            String Cognome,
            Date D_Nascita,
            String Email,
            String Password,
            String N_Telefono,
            Date D_Iscrizione,
            Boolean Se_Cliente,
            Boolean Verificato,
            Boolean Se_Privato,
            String Username,
            // Foto_Privato
            Boolean Se_Chef,
            // Foto_Chef
            // CV
            Boolean Se_Ristoratore,
            Boolean Deleted,
            Sede sede
    ) throws UserAlreadyKnownException;

    public void update (User user);

    public void delete (User user);

    public void updateUserPassword(User user, String newPassword);

    public User findByCF (String CF);
    public User findByCF1 (String CF);

    public User findByNomeCognome (String Nome, String Cognome);

    public User findByEmail (String Email);

    public List<User> findBySede(Sede sede);

    public List<User> getAllFreeChefs();
}
