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
            User Utente
    );

    public void update (Piatto piatto);

    public void delete (Piatto piatto);

    public void addPiattoinSede (Piatto piatto, Sede sede);

    // cerca un piatto per nome
    public List<Piatto> findByName (String Nome_Piatto);

    public List<Piatto> findByName (String Nome_Piatto, List<String> Allergeni);

    // cerca un piatto per ingrediente
    public List<Piatto> findByIngediente (Ingrediente Ingrediente);

    public List<Piatto> findByIngediente (Ingrediente Ingrediente, List<String> Allergeni);

    // cerca un piatto per sede
    public List<Piatto> findBySede (Sede Sede);

    // cerca un piatto per CF privato/chef/recensori
    public List<Piatto> findByCF (User CF_Utente);

    public Piatto findByIDPiatto(Integer ID_Piatto);

    public List<Piatto> findMostRecent();
}
