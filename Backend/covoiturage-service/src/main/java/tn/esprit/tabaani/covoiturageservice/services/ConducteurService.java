package tn.esprit.tabaani.covoiturageservice.services;

import tn.esprit.tabaani.covoiturageservice.entities.Conducteur;
import java.util.List;

public interface ConducteurService {

    Conducteur ajouter(Conducteur conducteur);

    List<Conducteur> lister();

    Conducteur trouverParId(Long id);

    Conducteur modifier(Long id, Conducteur conducteur);

    void supprimer(Long id);
}
