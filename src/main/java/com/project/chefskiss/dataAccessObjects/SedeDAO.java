package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Sede;

public interface SedeDAO {
    public Sede create (
            String Coordinate,
            String Via,
            String Citta,
            Integer Posti_Disponibili,
            Integer ID_Ristorante
    );

    public void update (Sede sede);

    public void delete (Sede sede);

    // cerca una sede dalla posizione
    public Sede findByPosition (String Via, String Citta);

    // cerca una sede per ristorante di appartenenza
    public Sede findByRistorante (String Ristorante);

    // cerca una sede per coordinate --> non so quanto possa servire però
    public Sede findByCoordinate (String Coordinate);
}
