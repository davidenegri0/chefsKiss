package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Contiene;
import com.project.chefskiss.modelObjects.Ingrediente;
import com.project.chefskiss.modelObjects.Piatto;

import java.util.List;

public interface ContieneDAO {
    public Contiene create (Piatto piatto, Ingrediente ingrediente, Integer quantita);
    public void update(Contiene contiene);
    public void delete(Contiene contiene);

    public List<Contiene> findByPiatto (Piatto piatto);
    public Contiene findByPiatto_Ingrediente (Piatto piatto, Ingrediente ingrediente);
}
