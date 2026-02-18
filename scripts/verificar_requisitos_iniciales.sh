#!/usr/bin/env bash
set -euo pipefail

check() {
  local label="$1"
  local path="$2"
  if [[ -e "$path" ]]; then
    echo "[OK] $label -> $path"
  else
    echo "[MISSING] $label -> $path"
  fi
}

echo "Verificación de requisitos iniciales"
check "Frontend SPA" "frontend-spa/package.json"
check "Event bus RabbitMQ" "event-bus/docker-compose.yml"
check "Microservicio notificaciones Go" "microservicio-notificaciones-go/cmd/server/main.go"
check "Documento de estado" "docs/requisitos-proyecto-inicial.md"
