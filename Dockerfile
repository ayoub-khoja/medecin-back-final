# Dockerfile pour déployer le backend Spring Boot sur Render

# Utiliser une image Java 17
FROM eclipse-temurin:17-jdk-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers Maven wrapper
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x ./mvnw

# Copier le fichier pom.xml
COPY pom.xml .

# Télécharger les dépendances (mise en cache)
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src ./src

# Builder l'application
RUN ./mvnw clean package -DskipTests

# Exposer le port (Render définit automatiquement le port via PORT)
EXPOSE 9000

# Commande de démarrage
# Render définit automatiquement la variable PORT
CMD ["sh", "-c", "java -jar -Dserver.port=${PORT:-9000} target/goldengymback-0.0.1-SNAPSHOT.jar"]
