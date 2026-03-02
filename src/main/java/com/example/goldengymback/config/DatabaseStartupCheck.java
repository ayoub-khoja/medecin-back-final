package com.example.goldengymback.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class DatabaseStartupCheck {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_HOST:}")
    private String dbHost;

    @Value("${DB_USER:}")
    private String dbUser;

    @PostConstruct
    public void checkDatabaseConfiguration() {
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
            System.err.println("2. Liez-la à votre service backend");
            System.err.println("3. OU définissez les variables d'environnement suivantes :");
            System.err.println("   - DB_HOST");
            System.err.println("   - DB_PORT (défaut: 5432)");
            System.err.println("   - DB_NAME");
            System.err.println("   - DB_USER");
            System.err.println("   - DB_PASSWORD");
            System.err.println("========================================");
            throw new RuntimeException(
                "Configuration PostgreSQL manquante. " +
                "Veuillez configurer DATABASE_URL ou les variables DB_* dans Render."
            );
        }
    }
}
