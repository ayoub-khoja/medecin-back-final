# Guide de Création de Compte Utilisateur

Ce guide explique comment créer un compte utilisateur pour se connecter à l'application.

## Méthode 1 : Via l'API REST (Recommandé)

### Endpoint d'inscription
**POST** `/api/users/register`

### Exemple avec cURL

```bash
curl -X POST http://localhost:9000/api/users/register \
  -H "Content-Type: application/json" \
  -d "{\"nom\":\"admin\",\"prenom\":\"Administrateur\",\"password\":\"admin123\"}"
```

### Exemple avec PowerShell

```powershell
$body = @{
    nom = "admin"
    prenom = "Administrateur"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:9000/api/users/register" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Format JSON requis

```json
{
  "nom": "nom_utilisateur",
  "prenom": "Prénom",
  "password": "mot_de_passe"
}
```

### Réponses possibles

- **201 Created** : Compte créé avec succès
  ```json
  {
    "id": 1,
    "nom": "admin",
    "prenom": "Administrateur",
    "password": "admin123"
  }
  ```

- **409 Conflict** : Un utilisateur avec ce nom existe déjà
- **400 Bad Request** : Champs requis manquants
- **500 Internal Server Error** : Erreur serveur

## Méthode 2 : Directement en Base de Données (SQL)

### Utiliser le script SQL fourni

1. Connectez-vous à MySQL sur le port **3307**
2. Sélectionnez la base de données `medecin`
3. Exécutez le script `create_user.sql` ou utilisez cette commande :

```sql
INSERT INTO user (nom, prenom, password) 
VALUES ('admin', 'Administrateur', 'admin123');
```

### Exemple avec MySQL en ligne de commande

```bash
mysql -u root -P 3307 medecin -e "INSERT INTO user (nom, prenom, password) VALUES ('admin', 'Administrateur', 'admin123');"
```

## Méthode 3 : Via le Frontend (si implémenté)

Si une page d'inscription existe dans le frontend, utilisez-la pour créer un compte.

## Connexion après création

Une fois le compte créé, vous pouvez vous connecter avec :

**POST** `/api/users/login`

```json
{
  "nom": "admin",
  "password": "admin123"
}
```

## Notes importantes

- ⚠️ **Sécurité** : Les mots de passe sont stockés en **clair** dans la base de données. Pour la production, il est fortement recommandé d'ajouter un hashage des mots de passe (BCrypt, etc.).
- Le nom d'utilisateur (`nom`) doit être unique
- Tous les champs sont requis pour la création d'un compte
