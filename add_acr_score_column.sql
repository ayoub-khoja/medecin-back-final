-- Ajouter la colonne acr_score pour stocker le score ACR pur
ALTER TABLE mammary_scan ADD COLUMN acr_score VARCHAR(10);

-- Mettre à jour les enregistrements existants si nécessaire
-- UPDATE mammary_scan SET acr_score = conclusionIA WHERE acr_score IS NULL; 