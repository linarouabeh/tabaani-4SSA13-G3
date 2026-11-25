package tn.esprit.tabaani.covoiturageservice.services.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tabaani.covoiturageservice.entities.Conducteur;
import tn.esprit.tabaani.covoiturageservice.repositories.ConducteurRepository;
import tn.esprit.tabaani.covoiturageservice.services.ConducteurService;

import java.util.List;

@Service
public class ConducteurServiceImpl implements ConducteurService {

    private final ConducteurRepository conducteurRepository;

    public ConducteurServiceImpl(ConducteurRepository conducteurRepository) {
        this.conducteurRepository = conducteurRepository;
    }

    @Override
    public Conducteur ajouter(Conducteur conducteur) {
        return conducteurRepository.save(conducteur);
    }

    @Override
    public List<Conducteur> lister() {
        return conducteurRepository.findAll();
    }

    @Override
    public Conducteur trouverParId(Long id) {
        return conducteurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Conducteur introuvable"));
    }

    @Override
    public Conducteur modifier(Long id, Conducteur conducteur) {
        Conducteur existant = trouverParId(id);

        existant.setUtilisateurId(conducteur.getUtilisateurId());
        existant.setNumeroPermis(conducteur.getNumeroPermis());
        existant.setVerifie(conducteur.getVerifie());

        return conducteurRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {
        Conducteur existant = trouverParId(id);
        conducteurRepository.delete(existant);
    }
}
