# Dockerfile pour déployer le backend Spring Boot sur Render
# Solution multi-stage : utilise Maven pour builder, JRE pour exécuter

# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier le fichier pom.xml
COPY pom.xml .

# Télécharger les dépendances (mise en cache)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Builder l'application
RUN mvn -DskipTests clean package

# ===== Run stage =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port (Render définit automatiquement le port via PORT)
EXPOSE 9000

# Commande de démarrage
# Render définit automatiquement la variable PORT
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-9000}"]
