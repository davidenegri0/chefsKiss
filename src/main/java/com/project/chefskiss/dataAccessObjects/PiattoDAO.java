package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.util.List;

public interface PiattoDAO {
    public Piatto create (
            String Nome_Piatto,
            String Preparazione,
            String CF_Utente
    );

    public void update (Piatto piatto);

    public void delete (Piatto piatto);

    // cerca un piatto per nome
    public List<Piatto> findByName (String Nome_Piatto);

    // cerca un piatto per ingrediente
    public List<Piatto> findByIngediente (Ingrediente Ingrediente);

    // cerca un piatto per sede
    public List<Piatto> findBySede (Sede Sede);

    // cerca un piatto per CF privato/chef/recensori
    public List<Piatto> findByCF (User CF_Utente);

    public Piatto findByIDPiatto(String ID_Piatto);

    public Piatto[] find4MostRecent();
}
