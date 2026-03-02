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

    @Bean
    @Primary
    public DataSource dataSource(
            @Value("${DB_PORT:5432}") String dbPort,
            @Value("${DB_NAME:medecin}") String dbName,
            @Value("${DB_PASSWORD:}") String dbPassword) {

        System.out.println("=== Configuration DataSource (profil prod) ===");
        System.out.println("DATABASE_URL present: " + (databaseUrl != null && !databaseUrl.isEmpty()));
        System.out.println("DB_HOST: " + (dbHost != null && !dbHost.isEmpty() ? dbHost : "NOT SET"));
        System.out.println("=============================================");

        // Priorité 1 : DATABASE_URL (format Render automatique)
        // Render peut fournir postgres:// ou postgresql://
        if (databaseUrl != null && !databaseUrl.isEmpty()
                && (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://"))) {
            try {
                // Normaliser : remplacer postgres:// par postgresql://
                String normalizedUrl = databaseUrl;
                if (normalizedUrl.startsWith("postgres://") && !normalizedUrl.startsWith("postgresql://")) {
                    normalizedUrl = "postgresql://" + normalizedUrl.substring("postgres://".length());
                }

                // Parser avec URI (remplacer le scheme pour éviter les erreurs)
                URI uri = new URI(normalizedUrl.replace("postgresql://", "http://"));
                String userInfo = uri.getUserInfo();
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                String dbNameFromUrl = (path != null && path.startsWith("/")) ? path.substring(1) : path;

                String username = "";
                String password = "";
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    username = credentials[0];
                    password = credentials.length > 1 ? credentials[1] : "";
                }

                String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, dbNameFromUrl);

                System.out.println("DataSource from DATABASE_URL -> " + jdbcUrl);
                System.out.println("Username: " + username);
                System.out.println("Host: " + host + ", Port: " + port + ", DB: " + dbNameFromUrl);

                return DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
            } catch (Exception e) {
                System.err.println("ERROR parsing DATABASE_URL: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Cannot configure DataSource from DATABASE_URL", e);
            }
        }

        // Priorité 2 : Variables séparées DB_HOST, DB_USER, DB_PASSWORD, DB_NAME, DB_PORT
        if (dbHost != null && !dbHost.isEmpty()) {
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", dbHost, dbPort, dbName);
            System.out.println("DataSource from separate vars -> " + jdbcUrl);

            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(dbUser)
                    .password(dbPassword)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }

        // Aucune configuration
        String msg = "ERREUR: Aucune config PostgreSQL trouvee! "
                + "Ajoutez DATABASE_URL dans Render (liez votre base PostgreSQL au service).";
        System.err.println(msg);
        throw new RuntimeException(msg);
    }
}
