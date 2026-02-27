# Test du Calcul ACR - Cas Réel

## Données d'Entrée (Basées sur les Images)

### Mammographie
- **Forme** : ovale
- **Contours** : spiculés  
- **Densité** : élevée
- **Localisation** : (à remplir)

### Échostructure
- **Forme** : ovale
- **Contours** : spiculés
- **Densité** : hypoéchogène
- **Orientation** : non parallèle
- **Comportement** : atténuation postérieure
- **Mesure** : (à remplir)

### Critères Supplémentaires
- **Asymétrie** : Non
- **Distorsion architecturale** : Non
- **Calcifications** : Non

## Calcul Théorique

### Score Mammographie
- Forme ovale : 1pt
- Contours spiculés : 4pts
- Densité élevée : 2pts
- **Total mammographie : 7pts**

### Score Échostructure
- Forme ovale : 1pt
- Contours spiculés : 4pts
- Densité hypoéchogène : 2pts
- Orientation non parallèle : 2pts
- Atténuation postérieure : 2pts
- **Total échostructure : 11pts**

### Score Total
- **Score mammographie** : 7pts
- **Score échostructure** : 11pts
- **Critères supplémentaires** : 0pts
- **TOTAL GLOBAL : 18pts**

## Classification ACR Attendue

| Score Total | ACR | Signification |
|-------------|-----|---------------|
| 18 | 5 | Très suspect de malignité |

**Résultat attendu : ACR 5**

## Test de l'API

### 1. Test du Calcul Principal
```bash
GET /api/mammary-scan/acr/{scanId}
```
**Résultat attendu :** "ACR : 5 (Type A). Biopsie immédiate : Très suspect de malignité, biopsie urgente et consultation chirurgicale."

### 2. Test du Debug Détaillé
```bash
GET /api/mammary-scan/debug-acr-calculation/{scanId}
```
**Résultat attendu :**
```json
{
  "scanId": "X",
  "densiteMammaire": "A",
  "asymetrie": false,
  "distorsionArchitecturale": false,
  "calcifications": false,
  "massesMammographie": [
    {
      "forme": "ovale",
      "contours": "spiculés",
      "densite": "élevée",
      "localisation": "...",
      "score": 7
    }
  ],
  "totalScoreMammographie": 7,
  "massesEchostructure": [
    {
      "forme": "ovale",
      "contours": "spiculés",
      "densite": "hypoéchogène",
      "orientation": "non parallèle",
      "comportement": "atténuation postérieure",
      "mesure": "...",
      "score": 11
    }
  ],
  "totalScoreEchostructure": 11,
  "scoreTotal": 18,
  "acrCalculated": "5",
  "acrScoreActuel": "5",
  "acrTypeActuel": "A",
  "conduiteATenir": "Biopsie immédiate : Très suspect de malignité, biopsie urgente et consultation chirurgicale."
}
```

## Vérification des Logs

Les logs doivent afficher :
```
🔍 Début du calcul ACR clinique pour le scan ID: X
📊 Analyse des masses mammographiques...
📋 Masse mammographie - Forme: ovale, Contours: spiculés, Densité: élevée → Score: 7
📊 Analyse des masses échostructure...
📋 Masse échostructure - Forme: ovale, Contours: spiculés, Densité: hypoéchogène, Orientation: non parallèle, Comportement: atténuation postérieure → Score: 11
📊 Score total calculé: 18 points
🎯 Score ACR 5 : Très suspect de malignité (≥10pts)
✅ Score ACR final déterminé: 5
```

## Si le Problème Persiste

Si l'ACR 4 est toujours affiché au lieu de l'ACR 5 :

1. **Vérifier les données en base** : Utiliser l'endpoint de debug pour voir les valeurs exactes
2. **Vérifier les logs** : S'assurer que le calcul est bien effectué
3. **Vérifier les seuils** : Confirmer que 18 points donnent bien ACR 5
4. **Vérifier la sauvegarde** : S'assurer que le score calculé est bien sauvegardé

## Points de Vérification

- [ ] Les données sont correctement saisies dans les formulaires
- [ ] Les masses sont bien associées au scan
- [ ] Le calcul cumule bien les scores mammographiques et échographiques
- [ ] Les seuils sont correctement appliqués (≥10pts = ACR 5)
- [ ] Le score calculé est bien sauvegardé en base
- [ ] L'endpoint retourne le bon score ACR 