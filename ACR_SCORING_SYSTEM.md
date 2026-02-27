# Système de Scoring ACR BI-RADS - Documentation Technique

## Vue d'ensemble

Ce document décrit le système de calcul automatique du score ACR (American College of Radiology) basé sur les critères BI-RADS (Breast Imaging Reporting and Data System) implémenté dans l'application.

## Principe de fonctionnement

### 1. Calcul Clinique Automatique (Priorité)

Le système calcule automatiquement le score ACR en analysant les caractéristiques cliniques des masses mammaires selon les critères BI-RADS :

#### Critères Mammographiques
- **Forme** : ovale (1pt), ronde (1pt), irrégulière (3pts)
- **Contours** : circonscrits (0pt), masqués (1pt), microlobulés (2pts), indistincts (3pts), spiculés (4pts)
- **Densité** : faible (0pt), intermédiaire (1pt), élevée (2pts)

#### Critères Échographiques
- **Forme** : ovale (1pt), ronde (1pt), irrégulière (3pts)
- **Contours** : circonscrits (0pt), indistincts (3pts), anguleux (3pts), microlobulés (2pts), spiculés (4pts)
- **Densité** : isoéchogène (0pt), anéchogène (0pt), hypoéchogène (2pts), haute (1pt), complexe (3pts)
- **Orientation** : parallèle (0pt), non parallèle (2pts)
- **Comportement** : neutre (0pt), renforcement postérieur (0pt), atténuation postérieure (2pts), combiné (1pt)

#### Critères Supplémentaires
- **Asymétrie** : +2pts
- **Distorsion architecturale** : +3pts
- **Calcifications** : +2pts

### 2. Classification ACR Finale (CORRIGÉE)

| Score Total | ACR | Signification | Conduite à tenir |
|-------------|-----|---------------|------------------|
| 0-2 | 1 | Normal | Surveillance standard |
| 3-4 | 2 | Bénin | Surveillance standard |
| 5-6 | 3 | Probablement bénin | Contrôle à 6 mois |
| 7-9 | 4 | Suspect | Biopsie recommandée |
| 10+ | 5 | Très suspect | Biopsie urgente |

## Endpoints API

### 1. Calcul ACR Principal
```
GET /api/mammary-scan/acr/{scanId}
```
Retourne le score ACR calculé avec la conduite à tenir.

### 2. Test du Calcul ACR
```
GET /api/mammary-scan/test-acr-calculation/{scanId}
```
Retourne les détails complets du calcul pour le débogage.

### 3. Debug Détaillé du Calcul ACR (NOUVEAU)
```
GET /api/mammary-scan/debug-acr-calculation/{scanId}
```
Retourne le calcul point par point avec tous les scores détaillés.

### 4. Score ACR avec Type
```
GET /api/mammary-scan/acr-with-type/{scanId}
```
Retourne le score ACR, le type et la conduite à tenir.

## Exemples de Calcul (CORRIGÉS)

### Exemple 1 : Masse Suspecte (ACR 4)
- **Mammographie** : Forme ovale (1pt) + Contours indistincts (3pts) + Densité élevée (2pts)
- **Total** : 6pts → ACR 4 (Suspect de malignité)

### Exemple 2 : Masse Très Suspecte (ACR 5) - CAS RÉEL
- **Mammographie** : Forme ovale (1pt) + Contours spiculés (4pts) + Densité élevée (2pts) = 7pts
- **Échographie** : Forme ovale (1pt) + Contours spiculés (4pts) + Densité hypoéchogène (2pts) + Orientation non parallèle (2pts) + Atténuation postérieure (2pts) = 11pts
- **Total** : 7 + 11 = 18pts → ACR 5 (Très suspect de malignité)

## Logs et Débogage

Le système génère des logs détaillés pour :
- Le calcul du score clinique point par point
- L'analyse des masses mammographiques et échographiques
- L'extraction des données
- Les réponses de l'IA
- Les erreurs et incohérences

## Correction des Problèmes

### Problème Identifié
Le système précédent ne cumulait pas correctement les scores des examens mammographiques et échographiques, et les seuils étaient trop bas.

### Solution Implémentée
1. **Cumul correct des scores** : Les scores mammographiques et échographiques sont maintenant additionnés
2. **Seuils ajustés** : ACR 5 à partir de 10 points au lieu de 8
3. **Logs détaillés** : Chaque étape du calcul est maintenant loggée
4. **Endpoint de debug** : Nouvel endpoint pour vérifier le calcul point par point

## Maintenance

### Ajout de Nouveaux Critères
1. Modifier la méthode `calculateAcrScoreFromClinicalData()`
2. Ajouter la logique de scoring dans les méthodes appropriées
3. Mettre à jour cette documentation

### Modification des Seuils
1. Ajuster les valeurs dans la méthode de classification finale
2. Tester avec des cas cliniques réels
3. Valider avec des radiologues

## Validation Clinique

Ce système doit être validé par des radiologues expérimentés avant utilisation en production. Les scores générés automatiquement doivent toujours être revus par un professionnel de santé.

## Test du Système

Pour tester le système avec vos données :
1. Utilisez l'endpoint `/debug-acr-calculation/{scanId}` pour voir le calcul détaillé
2. Vérifiez que le score total est correctement calculé
3. Confirmez que l'ACR 5 est bien attribué pour les scores ≥10 points 