package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Recensione;
import com.project.chefskiss.modelObjects.User;
import com.project.chefskiss.modelObjects.Valutazione;

import java.util.List;

public interface RecensioneDAO {
    public Recensione create(User utente, Piatto piatto, Integer voto, String commento);
    public void update(Recensione recensione);
    public void delete(Recensione recensione);
    public List<Recensione> miglioriPiatti (Integer numero);
}
