#!/bin/bash

# Script de test pour le service de mailing

echo "🧪 Test du service de mailing"

# URL de base
BASE_URL="http://localhost:8081"

# Test 1: Demande de réinitialisation de mot de passe
echo ""
echo "📧 Test 1: Demande de réinitialisation de mot de passe"
FORGOT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/password/forgot" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com"
  }')

echo "Réponse forgot password: $FORGOT_RESPONSE"

# Test 2: Validation d'un token (simulation)
echo ""
echo "🔍 Test 2: Validation d'un token (simulation)"
echo "Note: Ce test nécessite un vrai token généré par le système"
echo "Pour tester complètement, il faut:"
echo "1. Configurer un vrai email Gmail dans application.properties"
echo "2. Recevoir l'email avec le token"
echo "3. Utiliser ce token pour tester la réinitialisation"

# Test 3: Réinitialisation de mot de passe (simulation)
echo ""
echo "🔐 Test 3: Réinitialisation de mot de passe (simulation)"
echo "Pour tester complètement:"
echo "1. Récupérer le token depuis l'email"
echo "2. Utiliser ce token pour réinitialiser le mot de passe"

# Configuration requise
echo ""
echo "📋 Configuration requise pour tester complètement:"
echo ""
echo "1. Configurer Gmail dans application.properties:"
echo "   spring.mail.username=votre-email@gmail.com"
echo "   spring.mail.password=votre-mot-de-passe-app"
echo ""
echo "2. Activer l'authentification à deux facteurs sur Gmail"
echo "3. Générer un mot de passe d'application"
echo "4. Utiliser ce mot de passe dans la configuration"
echo ""
echo "🔗 Guide pour configurer Gmail:"
echo "https://support.google.com/accounts/answer/185833"
echo ""
echo "✅ Service de mailing configuré et prêt à être testé !" 