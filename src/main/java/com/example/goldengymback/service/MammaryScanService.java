package com.example.goldengymback.service;

import com.example.goldengymback.model.MammaryScan;

import java.util.List;
import java.util.Optional;

public interface MammaryScanService {

    // Crée ou ajoute un nouveau scan mammaire
    MammaryScan addMammaryScan(MammaryScan mammaryScan);

    /**
     * Génère un diagnostic IA à partir du scan spécifié.
     * Si les champs "conclusionIA" (ACR) ou "conduiteATenir" sont manquants,
     * l'IA est appelée avec les données cliniques disponibles pour produire un résultat complet.
     *
     * @param scanId ID du scan
     * @return Réponse brute de l'IA (incluant le score ACR et la conduite à tenir)
     */
    String getAcrScoreAndUpdate(Long scanId);

    // Supprime un scan mammaire par ID
    void delete(Long id);

    // Récupère tous les scans mammaires
    List<MammaryScan> getAllMammaryScans();

    // Récupère un scan mammaire par son ID
    Optional<MammaryScan> getMammaryScanById(Long id);

    // Recherche personnalisée : par densité mammaire
    List<MammaryScan> getByDensiteMammaire(String densiteMammaire);

    // Recherche personnalisée : par présence d'asymétrie
    List<MammaryScan> getByAsymetrie(Boolean asymetrie);
}
