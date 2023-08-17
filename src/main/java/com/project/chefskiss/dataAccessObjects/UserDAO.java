package com.project.chefskiss.dataAccessObjects;

import java.sql.Date;
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
            Boolean Se_Chef,
            Boolean Se_Ristoratore,
            String Coordinate_Sede
    );

    public void update (User user);

    public void delete (User user);

    public User findByCF (String CF);

    public User findByNomeCognome (String Nome, String Cognome);

    public User findByEmail (String Email);
}
