-- Script pour ajouter les champs CV à la table alternant_profile
-- À exécuter si la table existe déjà

-- Ajouter les colonnes CV si elles n'existent pas
ALTER TABLE alternant_profile 
ADD COLUMN IF NOT EXISTS cv_base64 TEXT,
ADD COLUMN IF NOT EXISTS cv_nom_fichier VARCHAR(255),
ADD COLUMN IF NOT EXISTS cv_date_upload DATE;

-- Créer un index sur cv_date_upload pour optimiser les requêtes
CREATE INDEX IF NOT EXISTS idx_alternant_cv_date ON alternant_profile(cv_date_upload);

-- Commentaires sur les nouvelles colonnes
COMMENT ON COLUMN alternant_profile.cv_base64 IS 'CV encodé en base64';
COMMENT ON COLUMN alternant_profile.cv_nom_fichier IS 'Nom original du fichier CV';
COMMENT ON COLUMN alternant_profile.cv_date_upload IS 'Date d''upload du CV'; 