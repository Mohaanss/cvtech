-- Script de mise à jour pour le stockage local des CV
-- Supprime la colonne cv_base64 et ajoute cv_original_name

-- Supprimer la colonne cv_base64 si elle existe
ALTER TABLE alternant_profile DROP COLUMN IF EXISTS cv_base64;

-- Ajouter la colonne cv_original_name si elle n'existe pas
ALTER TABLE alternant_profile ADD COLUMN IF NOT EXISTS cv_original_name VARCHAR(255);

-- Mettre à jour les commentaires
COMMENT ON COLUMN alternant_profile.cv_nom_fichier IS 'Nom du fichier stocké sur le disque (UUID + extension)';
COMMENT ON COLUMN alternant_profile.cv_original_name IS 'Nom original du fichier uploadé par l''utilisateur';
COMMENT ON COLUMN alternant_profile.cv_date_upload IS 'Date d''upload du CV'; 