# Configuration Gmail pour le service de mailing

## 🎯 Objectif
Configurer Gmail pour permettre l'envoi d'emails depuis l'application CVTech.

## 📋 Étapes de configuration

### 1. Activer l'authentification à deux facteurs
1. Allez sur [myaccount.google.com](https://myaccount.google.com)
2. Cliquez sur "Sécurité"
3. Activez "Validation en 2 étapes"

### 2. Générer un mot de passe d'application
1. Dans "Sécurité", cliquez sur "Mots de passe d'application"
2. Sélectionnez "Application" → "Autre (nom personnalisé)"
3. Entrez "CVTech" comme nom
4. Copiez le mot de passe généré (16 caractères)

### 3. Configurer application.properties
Remplacez dans `demo/src/main/resources/application.properties` :

```properties
# Configuration Email (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=votre-mot-de-passe-app-16-caracteres
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### 4. Tester la configuration
1. Redémarrez l'application
2. Testez avec le script : `./test_email_service.sh`

## 🔧 Configuration alternative (pour les tests)

### Option 1: MailHog (local)
```properties
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
```

### Option 2: Gmail avec App Passwords
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=xxxx xxxx xxxx xxxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 🧪 Test du service

### 1. Demande de réinitialisation
```bash
curl -X POST http://localhost:8081/api/password/forgot \
  -H "Content-Type: application/json" \
  -d '{"email": "test@example.com"}'
```

### 2. Validation d'un token
```bash
curl -X GET http://localhost:8081/api/password/validate-token/TOKEN_ICI
```

### 3. Réinitialisation de mot de passe
```bash
curl -X POST http://localhost:8081/api/password/reset \
  -H "Content-Type: application/json" \
  -d '{
    "token": "TOKEN_ICI",
    "newPassword": "nouveauMotDePasse123",
    "confirmPassword": "nouveauMotDePasse123"
  }'
```

## 📧 Templates d'emails

### Email de réinitialisation
- Template : `password-reset.html`
- Variables : `nom`, `resetLink`, `expirationHours`

### Email de confirmation
- Template : `password-changed.html`
- Variables : `nom`, `date`

## 🔒 Sécurité

### Tokens de réinitialisation
- Générés avec UUID
- Stockés temporairement en mémoire
- Expirent après utilisation
- En production, utiliser Redis

### Validation
- Vérification de l'existence de l'utilisateur
- Validation du format email
- Confirmation des mots de passe
- Rate limiting sur les endpoints

## 🚀 Production

### Recommandations
1. Utiliser un service d'email dédié (SendGrid, Mailgun)
2. Stocker les tokens en base de données avec expiration
3. Implémenter un système de logs pour les emails
4. Ajouter des métriques de monitoring

### Variables d'environnement
```properties
SPRING_MAIL_USERNAME=votre-email@gmail.com
SPRING_MAIL_PASSWORD=votre-mot-de-passe-app
```

## 📝 Notes importantes

- Ne jamais commiter les vrais mots de passe
- Utiliser des variables d'environnement en production
- Tester avec des emails valides
- Surveiller les logs d'erreur SMTP 