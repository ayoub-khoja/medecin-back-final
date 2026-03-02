package com.example.goldengymback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_HOST:}")
    private String dbHost;

    @Value("${DB_USER:}")
    private String dbUser;

    /**
     * Crée toujours une DataSource pour le profil prod.
     * Utilise DATABASE_URL si disponible, sinon les variables séparées.
     * Échoue avec un message clair si aucune configuration n'est trouvée.
     */
    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${DB_PORT:5432}") String dbPort,
            @Value("${DB_NAME:medecin}") String dbName,
            @Value("${DB_PASSWORD:}") String dbPassword) {
        
        // Si DATABASE_URL est fourni (format Render), le parser
        if (databaseUrl != null && !databaseUrl.isEmpty() && databaseUrl.startsWith("postgresql://")) {
            try {
                // Format: postgresql://user:password@host:port/dbname
                URI uri = new URI(databaseUrl.replace("postgresql://", "http://"));
                String userInfo = uri.getUserInfo();
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                String dbNameFromUrl = path.startsWith("/") ? path.substring(1) : path;
                
                String username = "";
                String password = "";
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    username = credentials[0];
                    password = credentials.length > 1 ? credentials[1] : "";
                }
                
                // Construire l'URL JDBC
                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, dbNameFromUrl);
                
                System.out.println("✓ Configuration DataSource depuis DATABASE_URL: " + jdbcUrl);
                
                return DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
            } catch (Exception e) {
                System.err.println("✗ Erreur lors du parsing de DATABASE_URL: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Impossible de configurer la DataSource depuis DATABASE_URL", e);
            }
        }
        
        // Sinon, utiliser les variables séparées
        if (dbHost != null && !dbHost.isEmpty() && !dbHost.equals("REQUIRED") &&
            dbUser != null && !dbUser.isEmpty() && !dbUser.equals("REQUIRED")) {
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
            System.out.println("✓ Configuration DataSource depuis variables séparées: " + jdbcUrl);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(dbUser)
                    .password(dbPassword)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        // Aucune configuration trouvée - créer une DataSource factice qui échouera avec un message clair
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
        
        // Créer une DataSource factice qui échouera lors de la connexion mais permettra à Hibernate de démarrer
        // L'URL pointe vers un host inexistant pour échouer immédiatement avec un message clair
        String fakeUrl = "jdbc:postgresql://POSTGRESQL_NOT_CONFIGURED:5432/PLEASE_CONFIGURE_DATABASE";
        System.err.println("⚠ Création d'une DataSource factice pour permettre le démarrage de Hibernate.");
        System.err.println("⚠ La connexion échouera avec un message d'erreur clair.");
        
        return DataSourceBuilder.create()
                .url(fakeUrl)
                .username("NOT_CONFIGURED")
                .password("NOT_CONFIGURED")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
