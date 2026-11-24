package tn.esprit.tabaani.lieux.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tabaani.lieux.entities.Categorie;
import tn.esprit.tabaani.lieux.entities.Lieu;
import tn.esprit.tabaani.lieux.entities.Tag;
import tn.esprit.tabaani.lieux.repositories.CategorieRepository;
import tn.esprit.tabaani.lieux.repositories.LieuRepository;
import tn.esprit.tabaani.lieux.repositories.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LieuService implements ILieuService {

    private final LieuRepository lieuRepository;
    private final CategorieRepository categorieRepository;
    private final TagRepository tagRepository;

    public LieuService(LieuRepository lieuRepository,
                       CategorieRepository categorieRepository,
                       TagRepository tagRepository) {
        this.lieuRepository = lieuRepository;
        this.categorieRepository = categorieRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Lieu ajouterLieu(Lieu lieu) {
        // Un ajout doit créer un nouvel identifiant.
        lieu.setId(null);

        associerCategorieEtTags(lieu);

        return lieuRepository.save(lieu);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lieu> afficherLieux() {
        return lieuRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Lieu afficherLieuParId(Long id) {
        return lieuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Lieu introuvable : " + id
                ));
    }

    @Override
    public Lieu modifierLieu(Long id, Lieu lieu) {
        Lieu lieuExistant = afficherLieuParId(id);

        associerCategorieEtTags(lieu);

        lieuExistant.setNom(lieu.getNom());
        lieuExistant.setDescription(lieu.getDescription());
        lieuExistant.setAdresse(lieu.getAdresse());
        lieuExistant.setLatitude(lieu.getLatitude());
        lieuExistant.setLongitude(lieu.getLongitude());
        lieuExistant.setCategorie(lieu.getCategorie());
        lieuExistant.setTags(lieu.getTags());

        return lieuRepository.save(lieuExistant);
    }

    @Override
    public void supprimerLieu(Long id) {
        Lieu lieu = afficherLieuParId(id);
        lieuRepository.delete(lieu);
    }

    private void associerCategorieEtTags(Lieu lieu) {

        // Vérifier la présence de l'identifiant de catégorie.
        if (lieu.getCategorie() == null
                || lieu.getCategorie().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "L'identifiant de la catégorie est obligatoire"
            );
        }

        Long categorieId = lieu.getCategorie().getId();

        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Catégorie introuvable : " + categorieId
                ));

        lieu.setCategorie(categorie);

        // Les tags sont facultatifs, mais doivent exister si fournis.
        Set<Tag> tagsExistants = new HashSet<>();

        if (lieu.getTags() != null) {
            for (Tag tag : lieu.getTags()) {

                if (tag == null || tag.getId() == null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Chaque tag doit avoir un identifiant"
                    );
                }

                Tag tagExistant = tagRepository.findById(tag.getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Tag introuvable : " + tag.getId()
                        ));

                tagsExistants.add(tagExistant);
            }
        }

        lieu.setTags(tagsExistants);
    }
}