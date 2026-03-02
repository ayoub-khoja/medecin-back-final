package com.example.goldengymback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
    @ConditionalOnExpression("!'${DATABASE_URL:}'.isEmpty() || (!'${DB_HOST:}'.isEmpty() && !'${DB_USER:}'.isEmpty())")
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
        if (dbHost != null && !dbHost.isEmpty() && dbUser != null && !dbUser.isEmpty()) {
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", dbHost, dbPort, dbName);
            System.out.println("✓ Configuration DataSource depuis variables séparées: " + jdbcUrl);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(dbUser)
                    .password(dbPassword)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        throw new RuntimeException(
            "Aucune configuration PostgreSQL trouvée. " +
            "Définissez soit DATABASE_URL, soit DB_HOST + DB_USER + DB_PASSWORD dans Render."
        );
    }
}
