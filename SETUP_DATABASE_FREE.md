# 🆓 Bases de Données MySQL GRATUITES pour Render

## ✅ Option 1 : Railway (GRATUIT - Recommandé) ⭐

Railway offre MySQL gratuitement avec 500 MB de stockage.

### Étapes :

1. **Créer un compte** :
   - Allez sur https://railway.app
   - Créez un compte avec GitHub (gratuit)

2. **Créer une base MySQL** :
   - Cliquez sur "New Project"
   - Cliquez sur "New" → "Database" → **"MySQL"**
   - Railway créera automatiquement une base MySQL

3. **Récupérer les informations** :
   - Cliquez sur votre base MySQL
   - Allez dans l'onglet "Variables"
   - Vous verrez :
     - `MYSQLHOST` (host)
     - `MYSQLPORT` (port, généralement 3306)
     - `MYSQLDATABASE` (nom de la base)
     - `MYSQLUSER` (username)
     - `MYSQLPASSWORD` (password)

4. **Configurer dans Render** :
   
   ```
   SPRING_DATASOURCE_URL = jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}?useSSL=true&serverTimezone=UTC
   SPRING_DATASOURCE_USERNAME = ${MYSQLUSER}
   SPRING_DATASOURCE_PASSWORD = ${MYSQLPASSWORD}
   ```

   **Exemple réel** :
   ```
   SPRING_DATASOURCE_URL = jdbc:mysql://containers-us-west-xxx.railway.app:3306/railway?useSSL=true&serverTimezone=UTC
   SPRING_DATASOURCE_USERNAME = root
   SPRING_DATASOURCE_PASSWORD = xxxxx
   ```

---

## ✅ Option 2 : Render PostgreSQL (GRATUIT)

Render propose PostgreSQL gratuitement, mais vous devrez modifier votre code.

### Étapes :

1. **Créer PostgreSQL sur Render** :
   - Dans Render Dashboard → **New +** → **PostgreSQL**
   - Nom : `medecin-db`
   - Plan : **Free** (gratuit)
   - Render vous donnera automatiquement `DATABASE_URL`

2. **Modifier le code** :

   **a) Modifier `pom.xml`** :
   ```xml
   <!-- Remplacer mysql-connector-java par postgresql -->
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```

   **b) Modifier `application-production.properties`** :
   ```properties
   spring.datasource.url=${DATABASE_URL}
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

   **c) Variables Render** :
   ```
   DATABASE_URL = [Render vous donne cette URL automatiquement]
   ```

---

## ✅ Option 3 : Supabase (GRATUIT)

Supabase offre PostgreSQL gratuitement (500 MB).

### Étapes :

1. **Créer un compte** :
   - Allez sur https://supabase.com
   - Créez un compte gratuit

2. **Créer un projet** :
   - Créez un nouveau projet
   - Récupérez les informations de connexion

3. **Modifier le code** (comme pour PostgreSQL ci-dessus)

---

## ✅ Option 4 : Aiven (GRATUIT avec limites)

Aiven offre MySQL gratuitement avec certaines limites.

### Étapes :

1. Allez sur https://aiven.io
2. Créez un compte gratuit
3. Créez un service MySQL
4. Récupérez les informations de connexion

---

## 🚀 Solution Rapide : Railway MySQL

**La solution la plus simple et compatible avec votre code actuel :**

1. **Railway** : https://railway.app
2. Créez un compte (gratuit)
3. Créez une base MySQL
4. Copiez les variables de connexion
5. Ajoutez-les dans Render

**Aucune modification de code nécessaire !** ✅

---

## 📝 Variables Render avec Railway

Une fois Railway configuré, ajoutez dans Render :

```
SPRING_PROFILES_ACTIVE = production
SPRING_DATASOURCE_URL = jdbc:mysql://[HOST_RAILWAY]:[PORT]/[DATABASE]?useSSL=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME = [USERNAME_RAILWAY]
SPRING_DATASOURCE_PASSWORD = [PASSWORD_RAILWAY]
OPENAI_API_KEY = [VOTRE_CLE_API_OPENAI]
OPENAI_API_ENDPOINT = https://archimatchaiagent.openai.azure.com
OPENAI_API_DEPLOYMENT = khoja-chat
```

---

## ⚠️ Important

- **Railway** : Gratuit, MySQL natif, pas de modifications de code
- **Render PostgreSQL** : Gratuit, mais nécessite de changer MySQL → PostgreSQL
- Les tables seront créées automatiquement avec `ddl-auto=update`
