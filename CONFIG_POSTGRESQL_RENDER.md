# 🐘 Configuration PostgreSQL sur Render

## ✅ Code Modifié

Le code a été modifié pour supporter PostgreSQL. Vous pouvez maintenant utiliser votre base PostgreSQL sur Render.

## 📋 Étapes de Configuration

### 1. Récupérer les Informations de Connexion PostgreSQL

Dans Render, sur votre service PostgreSQL :

1. **Cliquez sur "Connect"** (bouton en haut à droite)
2. **Copiez l'URL complète** qui ressemble à :
   ```
   postgresql://user:password@host:port/database
   ```
   
   OU récupérez séparément :
   - **Host** : `dpg-xxxxx-a.oregon-postgres.render.com`
   - **Port** : `5432`
   - **Database** : `medecin_xxxxx`
   - **Username** : `medecin_user`
   - **Password** : `xxxxx`

### 2. Configurer les Variables d'Environnement dans Render

Dans votre **Web Service** (pas la base de données), ajoutez/modifiez ces variables :

#### Option A : Utiliser DATABASE_URL (Recommandé)

Render PostgreSQL fournit automatiquement `DATABASE_URL`. Ajoutez simplement :

```
DATABASE_URL = [Copiez l'URL complète depuis le bouton Connect]
```

**Exemple** :
```
DATABASE_URL = postgresql://medecin_user:password123@dpg-xxxxx-a.oregon-postgres.render.com:5432/medecin_xxxxx
```

#### Option B : Utiliser les Variables Séparées

Si vous préférez, vous pouvez utiliser :

```
SPRING_DATASOURCE_URL = jdbc:postgresql://[HOST]:5432/[DATABASE]
SPRING_DATASOURCE_USERNAME = [USERNAME]
SPRING_DATASOURCE_PASSWORD = [PASSWORD]
```

**Exemple** :
```
SPRING_DATASOURCE_URL = jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/medecin_xxxxx
SPRING_DATASOURCE_USERNAME = medecin_user
SPRING_DATASOURCE_PASSWORD = password123
```

### 3. Variables Complètes pour Render

Dans votre **Web Service**, ajoutez toutes ces variables :

1. **SPRING_PROFILES_ACTIVE** = `production`

2. **DATABASE_URL** = `postgresql://[USER]:[PASSWORD]@[HOST]:5432/[DATABASE]`
   - OU utilisez les variables séparées ci-dessus

3. **OPENAI_API_KEY** = `[VOTRE_CLE_API_OPENAI]`

4. **OPENAI_API_ENDPOINT** = `https://archimatchaiagent.openai.azure.com`

5. **OPENAI_API_DEPLOYMENT** = `khoja-chat`

### 4. Redéployer

Une fois les variables configurées :
- Render redéploiera automatiquement
- Les tables seront créées automatiquement grâce à `ddl-auto=update`

## ✅ Vérification

Après le déploiement, vérifiez les logs dans Render. Vous devriez voir :
- ✅ Connexion à PostgreSQL réussie
- ✅ Tables créées automatiquement
- ✅ Application démarrée

## 🔄 Développement Local

Pour le développement local, vous pouvez continuer à utiliser MySQL. Le fichier `application.properties` utilise toujours MySQL pour le développement local.

## ⚠️ Notes Importantes

- **Production** : Utilise PostgreSQL (via `application-production.properties`)
- **Local** : Utilise MySQL (via `application.properties`)
- Les deux drivers sont maintenant dans `pom.xml`
- Les tables seront créées automatiquement au premier démarrage
