package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Ristorante;

public interface RistoranteDAO {
    public Ristorante create (
            String Nome_Ristorante,
            String CF_Ristoratore
    );

    public void update (Ristorante ristorante);
    public void delete (Ristorante ristorante);

    // cerca un ristorante per nome
    public Ristorante findByName (String Nome_Ristorante);

    // cerca un ristorante per Ristoratore
    public Ristorante findByRistoratore (String Ristoratore);
}
