package com.exemple.reclamation.service;

import com.exemple.reclamation.entity.Reclamation;
import com.exemple.reclamation.repository.ReclamationRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;

    public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }

    public Reclamation create(Reclamation reclamation) {
        reclamation.setId(null);
        reclamation.setDateCreation(null);
        return reclamationRepository.save(reclamation);
    }

    public List<Reclamation> findAll() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> findById(Long id) {
        return reclamationRepository.findById(id);
    }

    public Optional<Reclamation> update(Long id, Reclamation updatedReclamation) {
        return reclamationRepository.findById(id).map(reclamation -> {
            reclamation.setTitre(updatedReclamation.getTitre());
            reclamation.setDescription(updatedReclamation.getDescription());
            reclamation.setCategorieId(updatedReclamation.getCategorieId());
            reclamation.setStatutId(updatedReclamation.getStatutId());
            reclamation.setUtilisateurId(updatedReclamation.getUtilisateurId());
            reclamation.setReferenceType(updatedReclamation.getReferenceType());
            reclamation.setReferenceId(updatedReclamation.getReferenceId());
            return reclamationRepository.save(reclamation);
        });
    }

    public boolean delete(Long id) {
        if (!reclamationRepository.existsById(id)) {
            return false;
        }

        reclamationRepository.deleteById(id);
        return true;
    }
}
