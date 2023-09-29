SELECT p.*, AVG(r.Voto) as Media_Voti
FROM piatto p
LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto
GROUP BY p.ID_Piatto;

