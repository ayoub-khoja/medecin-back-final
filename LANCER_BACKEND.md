# 🚀 Guide pour lancer le Backend

## Prérequis

Avant de lancer le backend, assurez-vous d'avoir :

1. **Java 17** installé sur votre machine
   - Vérifiez avec : `java -version`
   - Si ce n'est pas installé, téléchargez-le depuis [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou [OpenJDK](https://adoptium.net/)

2. **MySQL** en cours d'exécution sur le port **3307**
   - La base de données `medecin` doit exister
   - Vérifiez que MySQL est démarré

3. **Maven** (optionnel, car le projet utilise Maven Wrapper `mvnw`)

## Méthode 1 : Via PowerShell (Recommandé pour Windows)

1. **Ouvrez un nouveau terminal PowerShell**

2. **Naviguez vers le répertoire du projet** :
```powershell
cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"
```

3. **Lancez le backend avec Maven Wrapper** :

   **Option A : Directement avec PowerShell** (si cela fonctionne) :
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   
   **Option B : Via cmd** (si PowerShell a des problèmes) :
   ```powershell
   cmd /c mvnw.cmd spring-boot:run
   ```
   
   **Option C : Avec chemin complet** :
   ```powershell
   & ".\mvnw.cmd" spring-boot:run
   ```
   
   **Important** : Dans PowerShell, utilisez `;` pour enchaîner les commandes, pas `&&` :
   ```powershell
   cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"; cmd /c mvnw.cmd spring-boot:run
   ```
   
   **Si aucune de ces méthodes ne fonctionne**, utilisez l'invite de commande Windows (cmd.exe) au lieu de PowerShell :
   ```cmd
   cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"
   mvnw.cmd spring-boot:run
   ```

4. **Attendez que le backend démarre**. Vous devriez voir des messages comme :
   - `Started GoldengymbackApplication in X.XXX seconds`
   - Le serveur écoute sur `http://localhost:9000`

## Méthode 2 : Via Git Bash ou Terminal Linux/Mac

1. **Ouvrez un terminal Git Bash**

2. **Naviguez vers le répertoire du projet** :
```bash
cd "C:\Users\ASUS\Desktop\all projets\projet-final-seif\medecin\medecin-back-final-master"
```

3. **Lancez le backend** :
```bash
./mvnw spring-boot:run
```

## Méthode 3 : Via votre IDE (IntelliJ IDEA, Eclipse, VS Code)

### IntelliJ IDEA / Eclipse :
1. Ouvrez le projet dans votre IDE
2. Localisez la classe `GoldengymbackApplication.java`
3. Clic droit → "Run 'GoldengymbackApplication'"

### VS Code :
1. Installez l'extension "Extension Pack for Java"
2. Ouvrez le fichier `GoldengymbackApplication.java`
3. Cliquez sur "Run" au-dessus de la méthode `main`

## Vérification que le backend fonctionne

Une fois le backend démarré, testez-le avec une de ces méthodes :

### Test 1 : Via PowerShell
```powershell
Invoke-RestMethod -Uri "http://localhost:9000/api/users/all" -Method GET
```

### Test 2 : Via navigateur
Ouvrez votre navigateur et allez sur :
```
http://localhost:9000/api/users/all
```

### Test 3 : Via curl (si installé)
```bash
curl http://localhost:9000/api/users/all
```

## Configuration de la base de données

Le backend est configuré pour se connecter à MySQL sur :
- **Host** : `localhost`
- **Port** : `3307`
- **Database** : `medecin`
- **Username** : `root`
- **Password** : (vide par défaut)

Si votre configuration MySQL est différente, modifiez le fichier :
`src/main/resources/application.properties`

## Port du backend

Le backend écoute sur le **port 9000** par défaut.

Si vous devez changer le port, modifiez dans `application.properties` :
```properties
server.port=9000
```

## Arrêter le backend

Pour arrêter le backend, appuyez sur **Ctrl+C** dans le terminal où il s'exécute.

## Problèmes courants

### Erreur : "Java not found"
- Installez Java 17
- Vérifiez que Java est dans votre PATH : `java -version`

### Erreur : "Cannot connect to MySQL"
- Vérifiez que MySQL est démarré
- Vérifiez que MySQL écoute sur le port 3307
- Vérifiez les identifiants dans `application.properties`

### Erreur : "Port 9000 already in use"
- Un autre processus utilise le port 9000
- Arrêtez l'autre processus ou changez le port dans `application.properties`

### Le backend démarre mais le frontend ne peut pas se connecter
- Vérifiez que le backend écoute bien sur `http://localhost:9000`
- Vérifiez la configuration CORS dans `CorsConfig.java`
- Vérifiez que le frontend pointe vers `http://localhost:9000/api`

## Logs

Les logs du backend s'affichent dans le terminal. Vous verrez :
- Les requêtes SQL (si `logging.level.org.hibernate.SQL=DEBUG`)
- Les requêtes HTTP entrantes
- Les erreurs éventuelles
