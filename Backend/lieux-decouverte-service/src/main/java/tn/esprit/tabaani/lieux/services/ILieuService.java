package tn.esprit.tabaani.lieux.services;

import tn.esprit.tabaani.lieux.entities.Lieu;

import java.util.List;

public interface ILieuService {

    Lieu ajouterLieu(Lieu lieu);

    List<Lieu> afficherLieux();

    Lieu afficherLieuParId(Long id);

    Lieu modifierLieu(Long id, Lieu lieu);

    void supprimerLieu(Long id);
}