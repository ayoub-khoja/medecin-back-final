-- Script SQL pour créer un utilisateur dans la base de données
-- Utilisez ce script pour créer un compte utilisateur directement en base de données

-- Exemple: Créer un utilisateur admin
INSERT INTO user (nom, prenom, password) 
VALUES ('admin', 'Administrateur', 'admin123');

-- Exemple: Créer un utilisateur médecin
INSERT INTO user (nom, prenom, password) 
VALUES ('medecin', 'Docteur', 'medecin123');

-- Exemple: Créer un utilisateur personnalisé
-- Remplacez les valeurs par vos propres données
-- INSERT INTO user (nom, prenom, password) 
-- VALUES ('votre_nom', 'votre_prenom', 'votre_mot_de_passe');

-- Pour voir tous les utilisateurs
-- SELECT * FROM user;
