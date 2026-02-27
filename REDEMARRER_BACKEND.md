# Comment redémarrer le backend

## Méthode 1 : Via le terminal PowerShell

1. Arrêtez le processus actuel (Ctrl+C dans le terminal où le backend tourne)
2. Redémarrez avec :
```powershell
cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"
.\mvnw.cmd spring-boot:run
```

## Méthode 2 : Via Git Bash

1. Arrêtez le processus actuel (Ctrl+C)
2. Redémarrez avec :
```bash
cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"
./mvnw spring-boot:run
```

## Vérification

Une fois redémarré, testez l'endpoint :
```powershell
$body = @{ nom = "test"; prenom = "Test"; password = "test123" } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:9000/api/users/register" -Method POST -ContentType "application/json" -Body $body
```

Vous devriez recevoir une réponse 201 avec les données de l'utilisateur créé.
