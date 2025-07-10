# Test du nouveau système d'upload de CV

## 🎯 Objectif
Tester le nouveau système de stockage local des fichiers PDF au lieu du base64.

## 📋 Prérequis
1. Backend démarré sur le port 8081
2. Frontend démarré sur le port 4200
3. Base de données PostgreSQL configurée

## 🔧 Configuration

### 1. Mise à jour de la base de données
```sql
-- Exécuter le script SQL
\i demo/update_cv_storage.sql
```

### 2. Vérifier la configuration
- Le dossier `uploads/cv/` sera créé automatiquement
- Taille maximale des fichiers : 10MB
- Seuls les fichiers PDF acceptés

## 🧪 Tests à effectuer

### Test 1 : Upload d'un CV
1. Se connecter en tant qu'alternant
2. Aller sur le dashboard alternant
3. Onglet "Gestion CV"
4. Sélectionner un fichier PDF
5. Cliquer sur "Uploader le CV"
6. ✅ Vérifier que le fichier apparaît dans `uploads/cv/`

### Test 2 : Téléchargement
1. Cliquer sur "Télécharger"
2. ✅ Vérifier que le fichier se télécharge correctement

### Test 3 : Remplacement
1. Uploader un nouveau CV
2. ✅ Vérifier que l'ancien fichier est supprimé du disque
3. ✅ Vérifier que le nouveau fichier est enregistré

### Test 4 : Suppression
1. Cliquer sur "Supprimer"
2. ✅ Vérifier que le fichier est supprimé du disque
3. ✅ Vérifier que les infos en base sont nettoyées

## 🔍 Vérifications techniques

### Backend
- [ ] Fichiers stockés dans `uploads/cv/`
- [ ] Noms de fichiers uniques (UUID)
- [ ] Validation des types MIME
- [ ] Validation de la taille
- [ ] Nettoyage automatique des anciens fichiers

### Frontend
- [ ] Upload avec FormData
- [ ] Validation côté client
- [ ] Messages d'erreur appropriés
- [ ] Téléchargement fonctionnel

### Base de données
- [ ] Colonne `cv_base64` supprimée
- [ ] Colonne `cv_original_name` ajoutée
- [ ] Stockage du nom de fichier uniquement

## 🚀 Avantages du nouveau système

1. **Performance** : Plus de base64 en base de données
2. **Espace** : Économie d'espace en base
3. **Sécurité** : Validation côté serveur
4. **Scalabilité** : Possibilité de migrer vers S3 plus tard
5. **Simplicité** : Gestion directe des fichiers

## 🔄 Migration vers S3 (optionnel)

Si vous voulez migrer vers S3 plus tard :

1. Ajouter les dépendances AWS SDK
2. Créer un service S3StorageService
3. Modifier FileStorageService pour utiliser S3
4. Configurer les credentials AWS
5. Mettre à jour les endpoints

## 📝 Notes

- Les fichiers sont stockés avec des noms uniques (UUID)
- L'ancien fichier est automatiquement supprimé lors d'un remplacement
- La validation se fait côté client ET serveur
- Le rate limiting est toujours actif pour les uploads 