SELECT p.*, AVG(r.Voto) as Media
FROM piatto p
LEFT JOIN recensisce r on p.ID_Piatto = r.ID_Piatto
GROUP BY p.ID_Piatto;


SELECT p.*, i.*
FROM piatto as p
LEFT JOIN contiene c on p.ID_Piatto = c.ID_Piatto
LEFT JOIN ingrediente i on c.Nome_Ingrediente = i.Nome_Ingrediente

