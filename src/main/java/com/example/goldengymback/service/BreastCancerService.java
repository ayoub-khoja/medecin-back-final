package com.example.goldengymback.service;

public interface BreastCancerService {

    // Diagnostic basé sur ID existant (scan déjà enregistré)
    String getAcrScore(Long scanId);

    // Diagnostic basé uniquement sur les données brutes (formulaire)
    String getDiagnosticFromData(String description);
}
