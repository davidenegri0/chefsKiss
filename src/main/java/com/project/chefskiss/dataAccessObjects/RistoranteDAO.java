package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Ristorante;

import java.util.List;

public interface RistoranteDAO {
    public Ristorante create (
            String Nome_Ristorante,
            String CF_Ristoratore
    );

    public void update (Ristorante ristorante);
    public void delete (Ristorante ristorante);

    public Ristorante findById(Integer id);

    // cerca un ristorante per nome
    public List<Ristorante> findByName (String ristorante);

    // cerca un ristorante per Ristoratore
    public Ristorante findByRistoratore (String ristorante);

    // ritorna tutti i ristoranti nel db
    public List<Ristorante> getAll ();
}
