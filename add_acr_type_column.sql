-- Script pour ajouter la colonne acr_type à la table mammary_scan
-- Exécutez ce script sur votre base de données pour ajouter le nouveau champ

ALTER TABLE mammary_scan ADD COLUMN acr_type VARCHAR(10);

-- Commentaire pour documenter la colonne
COMMENT ON COLUMN mammary_scan.acr_type IS 'Type ACR (A, B ou C) pour la densité mammaire'; 