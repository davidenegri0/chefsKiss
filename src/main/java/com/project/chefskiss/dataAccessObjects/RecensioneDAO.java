package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Recensione;
import com.project.chefskiss.modelObjects.User;

import java.util.List;

public interface RecensioneDAO {
    public Recensione create(User utente, Piatto piatto, Integer voto, String commento);
    public void update(Recensione recensione);
    public void delete(Recensione recensione);
    public void deleteByCF (String CF);
    public void deleteByPiatto (Integer ID);
    public List<Recensione> miglioriPiatti (Integer numero);
    public List<Recensione> findByPiatto (Integer id_piatto);
    public boolean checkRecensione(String CF_recensore, Integer id_piatto);
    public Recensione findByPiatto_Utente (Integer id_piatto, String CF_user);
}
