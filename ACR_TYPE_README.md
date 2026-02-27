# Modification pour l'affichage du type ACR

## Résumé des modifications

Cette modification permet d'afficher le type ACR (A, B ou C) en plus du score ACR dans les résultats.

## Fichiers modifiés

### 1. Modèle MammaryScan
- **Fichier**: `src/main/java/com/example/goldengymback/model/MammaryScan.java`
- **Modification**: Ajout du champ `acrType` pour stocker le type ACR (A, B ou C)

### 2. Service BreastCancerService
- **Fichier**: `src/main/java/com/example/goldengymback/serviceImpl/BreastCancerService.java`
- **Modification**: Mise à jour de la méthode `updateScanWithAiResponse` pour stocker le type ACR

### 3. Service MammaryScanServiceImpl
- **Fichier**: `src/main/java/com/example/goldengymback/serviceImpl/MammaryScanServiceImpl.java`
- **Modifications**:
  - Ajout de la méthode `extractAcrType()` pour extraire le type ACR de la réponse IA
  - Mise à jour de la logique de sauvegarde pour inclure le type ACR
  - Modification du prompt pour demander le type ACR à l'IA

### 4. Contrôleur MammaryScanController
- **Fichier**: `src/main/java/com/example/goldengymback/controller/MammaryScanController.java`
- **Modifications**:
  - Mise à jour de l'endpoint `/acr/{scanId}` pour afficher le type ACR
  - Ajout d'un nouvel endpoint `/acr-with-type/{scanId}` pour une réponse JSON structurée
  - L'endpoint `/all` inclut maintenant automatiquement le type ACR

## Base de données

### Script SQL
- **Fichier**: `add_acr_type_column.sql`
- **Action**: Exécutez ce script pour ajouter la colonne `acr_type` à votre table `mammary_scan`

```sql
ALTER TABLE mammary_scan ADD COLUMN acr_type VARCHAR(10);
```

## Nouveaux endpoints

### 1. Endpoint modifié: GET `/api/mammary-scan/acr/{scanId}`
- **Avant**: Retournait seulement le score ACR
- **Après**: Retourne "ACR : X (Type Y). [Conduite à tenir]"

### 2. Nouvel endpoint: GET `/api/mammary-scan/acr-with-type/{scanId}`
- **Réponse JSON**:
```json
{
  "acrScore": "3",
  "acrType": "B",
  "conduiteATenir": "Biopsie",
  "fullAcr": "3 (Type B)"
}
```

## Format de réponse IA attendu

L'IA doit maintenant répondre dans le format :
```
ACR : X (Type Y). Action recommandée : [Surveillance/Biopsie/Ablation chirurgicale/Traitement médical]
```

Exemple :
```
ACR : 3 (Type B). Action recommandée : Biopsie
```

## Déploiement

1. Exécutez le script SQL `add_acr_type_column.sql` sur votre base de données
2. Redémarrez l'application Spring Boot
3. Les nouveaux scans incluront automatiquement le type ACR
4. Les anciens scans auront un type ACR null jusqu'à ce qu'ils soient recalculés 