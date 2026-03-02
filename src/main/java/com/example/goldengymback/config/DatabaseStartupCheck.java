package com.example.goldengymback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("prod")
public class DatabaseStartupCheck implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Environment env = event.getEnvironment();
        String databaseUrl = env.getProperty("DATABASE_URL", "");
        String dbHost = env.getProperty("DB_HOST", "");
        String dbUser = env.getProperty("DB_USER", "");

        boolean hasDatabaseUrl = databaseUrl != null && !databaseUrl.isEmpty();
        boolean hasSeparateVars = dbHost != null && !dbHost.isEmpty() && 
                                   !dbHost.equals("REQUIRED") &&
                                   dbUser != null && !dbUser.isEmpty() && 
                                   !dbUser.equals("REQUIRED");

        if (!hasDatabaseUrl && !hasSeparateVars) {
            System.err.println("========================================");
            System.err.println("ERREUR : Configuration PostgreSQL manquante");
            System.err.println("========================================");
            System.err.println("Aucune configuration PostgreSQL trouvée dans Render.");
            System.err.println("");
            System.err.println("Pour corriger cela :");
            System.err.println("1. Créez une base PostgreSQL dans Render");
            System.err.println("2. Liez-la à votre service backend (cela ajoutera automatiquement DATABASE_URL)");
            System.err.println("3. OU définissez les variables d'environnement suivantes dans Render :");
            System.err.println("   - DB_HOST (ex: dpg-xxxxx-a.oregon-postgres.render.com)");
            System.err.println("   - DB_PORT (défaut: 5432)");
            System.err.println("   - DB_NAME (ex: medecin_db)");
            System.err.println("   - DB_USER (ex: medecin_user)");
            System.err.println("   - DB_PASSWORD");
            System.err.println("");
            System.err.println("Puis redéployez votre service.");
            System.err.println("========================================");
            throw new RuntimeException(
                "Configuration PostgreSQL manquante. " +
                "Veuillez configurer DATABASE_URL ou les variables DB_* dans Render."
            );
        }
    }
}
