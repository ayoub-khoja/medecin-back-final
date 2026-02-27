# 🚀 Guide de Déploiement sur Render

## Prérequis

1. Un compte Render (gratuit disponible)
2. Une base de données MySQL (Render propose des bases de données MySQL)
3. Votre projet sur GitHub

## Étapes de Déploiement

### 1. Préparer le projet sur GitHub

Assurez-vous que votre projet backend est bien poussé sur GitHub :
```bash
git add .
git commit -m "Prepare for Render deployment"
git push origin master
```

### 2. Créer une base de données MySQL sur Render

1. Allez sur [Render Dashboard](https://dashboard.render.com)
2. Cliquez sur **"New +"** → **"PostgreSQL"** ou **"MySQL"**
3. Choisissez **"MySQL"** (si disponible) ou utilisez une base de données externe
4. Notez les informations de connexion :
   - Host
   - Port
   - Database name
   - Username
   - Password

### 3. Créer le service Web sur Render

1. Allez sur [Render Dashboard](https://dashboard.render.com)
2. Cliquez sur **"New +"** → **"Web Service"**
3. Connectez votre repository GitHub
4. Sélectionnez le repository contenant votre backend
5. Configurez les paramètres :

#### Configuration de base :
- **Name** : `medecin-backend` (ou le nom de votre choix)
- **Environment** : `Java`
- **Build Command** : `./mvnw clean package -DskipTests`
- **Start Command** : `java -jar target/goldengymback-0.0.1-SNAPSHOT.jar`

#### Variables d'environnement à ajouter :

Dans la section **"Environment Variables"**, ajoutez :

```
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:mysql://[HOST]:[PORT]/[DATABASE]?useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=[USERNAME]
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
OPENAI_API_KEY=[VOTRE_CLE_API_OPENAI]
OPENAI_API_ENDPOINT=https://archimatchaiagent.openai.azure.com
OPENAI_API_DEPLOYMENT=khoja-chat
JAVA_VERSION=17
```

**Remplacez** `[HOST]`, `[PORT]`, `[DATABASE]`, `[USERNAME]`, `[PASSWORD]` par les valeurs de votre base de données MySQL.

### 4. Déployer

1. Cliquez sur **"Create Web Service"**
2. Render va automatiquement :
   - Cloner votre repository
   - Exécuter le build command
   - Démarrer votre application avec le start command

### 5. Vérifier le déploiement

Une fois le déploiement terminé, vous obtiendrez une URL comme :
`https://medecin-backend.onrender.com`

Testez votre API :
```bash
curl https://medecin-backend.onrender.com/api/mammary-scan/all
```

## Configuration Alternative (Sans render.yaml)

Si vous préférez configurer manuellement dans le dashboard Render :

### Build Command :
```
./mvnw clean package -DskipTests
```

### Start Command :
```
java -jar target/goldengymback-0.0.1-SNAPSHOT.jar
```

## Notes Importantes

1. **Base de données** : Si Render ne propose pas MySQL, vous pouvez utiliser :
   - Une base de données MySQL externe (PlanetScale, AWS RDS, etc.)
   - Ou migrer vers PostgreSQL (nécessite des modifications du code)

2. **Port** : Render définit automatiquement le port via la variable `PORT`. Votre application doit utiliser `${PORT}` au lieu d'un port fixe.

3. **Timeout** : Les services gratuits sur Render peuvent avoir des timeouts. Pour les appels OpenAI qui peuvent prendre du temps, envisagez d'augmenter les timeouts.

4. **Logs** : Consultez les logs dans le dashboard Render pour diagnostiquer les problèmes.

## Dépannage

### Erreur de build
- Vérifiez que `mvnw` est exécutable (chmod +x mvnw)
- Vérifiez que Java 17 est disponible

### Erreur de connexion à la base de données
- Vérifiez les variables d'environnement `SPRING_DATASOURCE_*`
- Vérifiez que la base de données accepte les connexions externes
- Vérifiez le firewall de la base de données

### Application ne démarre pas
- Vérifiez les logs dans Render Dashboard
- Vérifiez que le port est bien configuré avec `${PORT}`
- Vérifiez que toutes les variables d'environnement sont définies
