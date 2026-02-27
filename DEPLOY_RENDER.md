# 🚀 Déploiement Backend sur Render - Guide Rapide

## Configuration dans Render Dashboard

### 1. Créer un nouveau Web Service

1. Allez sur https://dashboard.render.com
2. Cliquez sur **"New +"** → **"Web Service"**
3. Connectez votre repository GitHub
4. Sélectionnez le repository `medecin-back-final` (ou votre repo backend)

### 2. Configuration du Service

#### Informations de base :
- **Name** : `medecin-backend`
- **Region** : Choisissez la région la plus proche
- **Branch** : `master` (ou votre branche principale)
- **Root Directory** : Laissez vide (racine du projet)
- **Environment** : **IMPORTANT** : Sélectionnez **"Docker"** (pas Node.js, pas Java directement)

#### Build & Deploy :

**⚠️ IMPORTANT : Configuration Docker**

**Environment** : Sélectionnez **"Docker"** dans le menu déroulant (pas Node.js, pas Java)

**Dockerfile Path** :
```
./Dockerfile
```

**OU si vous préférez sans Docker :**

**Environment** : Sélectionnez **"Java"** (pas Node.js)

**Build Command** :
```bash
chmod +x ./mvnw && ./mvnw clean package -DskipTests
```

**Start Command** :
```bash
java -jar -Dserver.port=$PORT target/goldengymback-0.0.1-SNAPSHOT.jar
```

### 3. Variables d'Environnement

Dans la section **"Environment"**, ajoutez ces variables :

| Variable | Valeur | Description |
|---------|--------|-------------|
| `SPRING_PROFILES_ACTIVE` | `production` | Active le profil production |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://[HOST]:[PORT]/[DB]?useSSL=true&serverTimezone=UTC` | URL de votre base MySQL |
| `SPRING_DATASOURCE_USERNAME` | `[USERNAME]` | Nom d'utilisateur MySQL |
| `SPRING_DATASOURCE_PASSWORD` | `[PASSWORD]` | Mot de passe MySQL |
| `OPENAI_API_KEY` | `[VOTRE_CLE]` | Clé API OpenAI |
| `OPENAI_API_ENDPOINT` | `https://archimatchaiagent.openai.azure.com` | Endpoint OpenAI |
| `OPENAI_API_DEPLOYMENT` | `khoja-chat` | Déploiement OpenAI |
| `JAVA_VERSION` | `17` | Version Java |

**⚠️ IMPORTANT** : Remplacez `[HOST]`, `[PORT]`, `[DB]`, `[USERNAME]`, `[PASSWORD]` par les valeurs de votre base de données MySQL.

### 4. Base de Données MySQL

#### Option A : Base de données externe
- Utilisez une base MySQL existante (PlanetScale, AWS RDS, etc.)
- Utilisez les informations de connexion dans les variables d'environnement

#### Option B : Créer une base sur Render (si disponible)
1. **New +** → **PostgreSQL** (Render propose PostgreSQL, pas MySQL directement)
2. Ou utilisez un service externe comme [PlanetScale](https://planetscale.com) (gratuit)

### 5. Déployer

1. Cliquez sur **"Create Web Service"**
2. Attendez que le build se termine (5-10 minutes)
3. Votre API sera disponible sur : `https://medecin-backend.onrender.com`

## 🔧 Configuration CORS pour le Frontend

Après le déploiement, mettez à jour `CorsConfig.java` avec l'URL de votre frontend déployé :

```java
.allowedOrigins(
    "http://localhost:5173",
    "https://votre-frontend.onrender.com" // URL de votre frontend
)
```

## ✅ Vérification

Testez votre API déployée :
```bash
curl https://medecin-backend.onrender.com/api/mammary-scan/all
```

## 📝 Notes

- **Plan gratuit** : Le service peut "s'endormir" après 15 minutes d'inactivité
- **Premier démarrage** : Peut prendre 30-60 secondes
- **Logs** : Disponibles dans le dashboard Render
- **Variables sensibles** : Ne jamais commiter les clés API dans le code

## 🐛 Dépannage

### Build échoue
- Vérifiez que `mvnw` est présent dans le repository
- Vérifiez les logs de build dans Render

### Application ne démarre pas
- Vérifiez les logs dans Render Dashboard
- Vérifiez que toutes les variables d'environnement sont définies
- Vérifiez la connexion à la base de données

### Erreur de connexion MySQL
- Vérifiez que la base accepte les connexions externes
- Vérifiez le firewall
- Utilisez `useSSL=true` dans l'URL de connexion
