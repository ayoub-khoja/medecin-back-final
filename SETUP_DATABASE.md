# 🗄️ Configuration de la Base de Données pour Render

## ❌ Problème Actuel

Render ne peut **PAS** accéder à `localhost` car c'est un service cloud. Vous devez utiliser une base de données hébergée.

## ✅ Solutions Recommandées

### Option 1 : PlanetScale (GRATUIT - Recommandé) ⭐

PlanetScale offre MySQL gratuitement et est compatible avec votre code existant.

#### Étapes :

1. **Créer un compte** :
   - Allez sur https://planetscale.com
   - Créez un compte gratuit (pas besoin de carte bancaire)

2. **Créer une base de données** :
   - Cliquez sur "Create database"
   - Nom : `medecin` (ou autre)
   - Région : Choisissez la plus proche
   - Plan : **Free** (gratuit)

3. **Récupérer les informations de connexion** :
   - Allez dans votre base de données
   - Cliquez sur "Connect"
   - Sélectionnez "General" (pas Branch)
   - Copiez les informations :
     - **Host** : `xxxxx.psdb.cloud`
     - **Username** : `xxxxx`
     - **Password** : `pscale_pw_xxxxx`
     - **Database** : `medecin`

4. **Configurer dans Render** :
   
   Dans les variables d'environnement de Render, modifiez :

   ```
   SPRING_DATASOURCE_URL = jdbc:mysql://[HOST_FROM_PLANETSCALE]:3306/[DATABASE]?useSSL=true&serverTimezone=UTC&requireSSL=true
   SPRING_DATASOURCE_USERNAME = [USERNAME_FROM_PLANETSCALE]
   SPRING_DATASOURCE_PASSWORD = [PASSWORD_FROM_PLANETSCALE]
   ```

   **Exemple réel** :
   ```
   SPRING_DATASOURCE_URL = jdbc:mysql://aws.connect.psdb.cloud:3306/medecin?useSSL=true&serverTimezone=UTC&requireSSL=true
   SPRING_DATASOURCE_USERNAME = abc123xyz
   SPRING_DATASOURCE_PASSWORD = pscale_pw_abc123xyz
   ```

5. **Créer les tables** :
   - PlanetScale créera automatiquement les tables grâce à `spring.jpa.hibernate.ddl-auto=update`
   - Ou vous pouvez importer votre schéma SQL existant

---

### Option 2 : Render PostgreSQL (GRATUIT mais nécessite des modifications)

Render propose PostgreSQL gratuitement, mais vous devrez modifier votre code pour utiliser PostgreSQL au lieu de MySQL.

#### Étapes :

1. **Créer une base PostgreSQL sur Render** :
   - Dans Render Dashboard → **New +** → **PostgreSQL**
   - Nom : `medecin-db`
   - Plan : **Free**

2. **Récupérer les informations** :
   - Render vous donnera automatiquement :
     - `DATABASE_URL` (contient tout)
     - Ou séparément : Host, Port, Database, Username, Password

3. **Modifier le code** (nécessaire) :
   - Changer `mysql-connector-java` → `postgresql` dans `pom.xml`
   - Changer `MySQLDialect` → `PostgreSQLDialect` dans `application.properties`
   - Adapter les requêtes SQL si nécessaire

---

### Option 3 : AWS RDS / Google Cloud SQL (Payant)

Pour une solution plus robuste en production, mais payante.

---

## 🚀 Configuration Rapide avec PlanetScale

### 1. Créer la base sur PlanetScale

1. Allez sur https://planetscale.com/sign-up
2. Créez un compte
3. Créez une nouvelle base de données nommée `medecin`
4. Cliquez sur "Connect" → "General"
5. Copiez les informations de connexion

### 2. Variables d'environnement dans Render

Remplacez ces valeurs dans Render :

```
SPRING_DATASOURCE_URL = jdbc:mysql://[VOTRE_HOST_PLANETSCALE]:3306/medecin?useSSL=true&serverTimezone=UTC&requireSSL=true
SPRING_DATASOURCE_USERNAME = [VOTRE_USERNAME_PLANETSCALE]
SPRING_DATASOURCE_PASSWORD = [VOTRE_PASSWORD_PLANETSCALE]
```

### 3. Redéployer

Une fois les variables mises à jour, Render redéploiera automatiquement.

---

## 📝 Exemple Complet de Variables Render avec PlanetScale

```
SPRING_PROFILES_ACTIVE = production
SPRING_DATASOURCE_URL = jdbc:mysql://aws.connect.psdb.cloud:3306/medecin?useSSL=true&serverTimezone=UTC&requireSSL=true
SPRING_DATASOURCE_USERNAME = abc123def456
SPRING_DATASOURCE_PASSWORD = pscale_pw_xyz789
OPENAI_API_KEY = [VOTRE_CLE_API_OPENAI]
OPENAI_API_ENDPOINT = https://archimatchaiagent.openai.azure.com
OPENAI_API_DEPLOYMENT = khoja-chat
```

---

## ⚠️ Important

- **Ne jamais utiliser `localhost`** dans Render
- **PlanetScale est gratuit** et compatible MySQL
- Les tables seront créées automatiquement au premier démarrage grâce à `ddl-auto=update`
