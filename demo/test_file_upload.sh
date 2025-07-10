#!/bin/bash

# Script de test pour l'upload de CV avec stockage local

echo "🧪 Test d'upload de CV avec stockage local"

# URL de base
BASE_URL="http://localhost:8081"

# Créer un fichier PDF de test
echo "📄 Création d'un fichier PDF de test..."
echo "%PDF-1.4" > test_cv.pdf
echo "1 0 obj" >> test_cv.pdf
echo "<<" >> test_cv.pdf
echo "/Type /Catalog" >> test_cv.pdf
echo "/Pages 2 0 R" >> test_cv.pdf
echo ">>" >> test_cv.pdf
echo "endobj" >> test_cv.pdf
echo "2 0 obj" >> test_cv.pdf
echo "<<" >> test_cv.pdf
echo "/Type /Pages" >> test_cv.pdf
echo "/Kids [3 0 R]" >> test_cv.pdf
echo "/Count 1" >> test_cv.pdf
echo ">>" >> test_cv.pdf
echo "endobj" >> test_cv.pdf
echo "3 0 obj" >> test_cv.pdf
echo "<<" >> test_cv.pdf
echo "/Type /Page" >> test_cv.pdf
echo "/Parent 2 0 R" >> test_cv.pdf
echo "/MediaBox [0 0 612 792]" >> test_cv.pdf
echo "/Contents 4 0 R" >> test_cv.pdf
echo ">>" >> test_cv.pdf
echo "endobj" >> test_cv.pdf
echo "4 0 obj" >> test_cv.pdf
echo "<<" >> test_cv.pdf
echo "/Length 44" >> test_cv.pdf
echo ">>" >> test_cv.pdf
echo "stream" >> test_cv.pdf
echo "BT" >> test_cv.pdf
echo "/F1 12 Tf" >> test_cv.pdf
echo "72 720 Td" >> test_cv.pdf
echo "(Test CV) Tj" >> test_cv.pdf
echo "ET" >> test_cv.pdf
echo "endstream" >> test_cv.pdf
echo "endobj" >> test_cv.pdf
echo "xref" >> test_cv.pdf
echo "0 5" >> test_cv.pdf
echo "0000000000 65535 f " >> test_cv.pdf
echo "0000000009 00000 n " >> test_cv.pdf
echo "0000000058 00000 n " >> test_cv.pdf
echo "0000000115 00000 n " >> test_cv.pdf
echo "0000000214 00000 n " >> test_cv.pdf
echo "trailer" >> test_cv.pdf
echo "<<" >> test_cv.pdf
echo "/Size 5" >> test_cv.pdf
echo "/Root 1 0 R" >> test_cv.pdf
echo ">>" >> test_cv.pdf
echo "startxref" >> test_cv.pdf
echo "364" >> test_cv.pdf
echo "%%EOF" >> test_cv.pdf

echo "✅ Fichier PDF de test créé"

# Test 1: Login pour obtenir un token
echo ""
echo "🔐 Test 1: Login"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/utilisateurs/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "motDePasse": "password123"
  }')

echo "Réponse login: $LOGIN_RESPONSE"

# Extraire le token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ Échec de la connexion"
    exit 1
fi

echo "✅ Token obtenu: ${TOKEN:0:20}..."

# Test 2: Upload du CV
echo ""
echo "📤 Test 2: Upload du CV"
UPLOAD_RESPONSE=$(curl -s -X POST "$BASE_URL/api/cv/upload" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@test_cv.pdf")

echo "Réponse upload: $UPLOAD_RESPONSE"

# Test 3: Récupérer les infos du CV
echo ""
echo "📋 Test 3: Récupérer les infos du CV"
INFO_RESPONSE=$(curl -s -X GET "$BASE_URL/api/cv/info" \
  -H "Authorization: Bearer $TOKEN")

echo "Réponse info: $INFO_RESPONSE"

# Test 4: Télécharger le CV
echo ""
echo "📥 Test 4: Télécharger le CV"
curl -s -X GET "$BASE_URL/api/cv/download" \
  -H "Authorization: Bearer $TOKEN" \
  -o downloaded_cv.pdf

if [ -f "downloaded_cv.pdf" ]; then
    echo "✅ CV téléchargé avec succès"
    ls -la downloaded_cv.pdf
else
    echo "❌ Échec du téléchargement"
fi

# Test 5: Supprimer le CV
echo ""
echo "🗑️ Test 5: Supprimer le CV"
DELETE_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/cv/delete" \
  -H "Authorization: Bearer $TOKEN")

echo "Réponse suppression: $DELETE_RESPONSE"

# Nettoyage
echo ""
echo "🧹 Nettoyage..."
rm -f test_cv.pdf downloaded_cv.pdf

echo ""
echo "✅ Tests terminés !"
echo ""
echo "📁 Les fichiers sont maintenant stockés dans: uploads/cv/"
echo "💾 Plus de base64 en base de données !" 