#!/bin/bash

# Script de test pour le rate limiting
# Usage: ./test-rate-limit.sh

BASE_URL="http://localhost:8081"
API_URL="$BASE_URL/api"

echo "🧪 Test du Rate Limiting"
echo "========================"

# Test 1: Rate limiting global
echo ""
echo "📊 Test 1: Rate limiting global (100 req/min)"
echo "----------------------------------------------"

for i in {1..105}; do
    response=$(curl -s -w "%{http_code}" -o /dev/null "$API_URL/test/rate-limit")
    
    if [ "$response" = "200" ]; then
        echo "✅ Requête $i: OK"
    elif [ "$response" = "429" ]; then
        echo "⛔ Requête $i: Rate limit exceeded (429)"
        break
    else
        echo "❌ Requête $i: Erreur ($response)"
    fi
    
    # Pause entre les requêtes
    sleep 0.1
done

# Test 2: Rate limiting pour endpoints sensibles
echo ""
echo "📊 Test 2: Rate limiting endpoints sensibles (10 req/min)"
echo "--------------------------------------------------------"

for i in {1..15}; do
    response=$(curl -s -w "%{http_code}" -o /dev/null -X POST "$API_URL/test/sensitive")
    
    if [ "$response" = "200" ]; then
        echo "✅ Requête $i: OK"
    elif [ "$response" = "429" ]; then
        echo "⛔ Requête $i: Rate limit exceeded (429)"
        break
    else
        echo "❌ Requête $i: Erreur ($response)"
    fi
    
    # Pause entre les requêtes
    sleep 0.1
done

# Test 3: Headers de rate limiting
echo ""
echo "📊 Test 3: Headers de rate limiting"
echo "-----------------------------------"

headers=$(curl -s -I "$API_URL/test/headers" | grep -i "x-ratelimit")
echo "Headers de rate limiting:"
echo "$headers"

echo ""
echo "✅ Tests terminés !" 