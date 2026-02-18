# Requisitos del proyecto inicial (secuencial)

Este documento valida, en orden, los requisitos que faltaban del blueprint objetivo y su estado en el monorepo.

## 1) Frontend SPA (React + TypeScript)

- **Objetivo:** contar con una SPA para interfaz de usuario.
- **Implementación inicial:** carpeta `frontend-spa/` con scaffold Vite + React + TypeScript.
- **Estado:** ✅ **Cumplido (MVP)**.
- **Evidencia:** `frontend-spa/package.json`, `frontend-spa/src/main.tsx`.

## 2) Mensajería/event-driven (Kafka o RabbitMQ)

- **Objetivo:** incluir un bus para comunicación asíncrona.
- **Implementación inicial:** stack RabbitMQ en `event-bus/docker-compose.yml`.
- **Estado:** ✅ **Cumplido (MVP)**.
- **Evidencia:** `event-bus/docker-compose.yml`.

## 3) Microservicio de notificaciones en Go + consumidor de eventos

- **Objetivo:** servicio independiente en Go orientado a eventos.
- **Implementación inicial:** `microservicio-notificaciones-go/` con endpoint `/health` y consumidor de cola `stock.low`.
- **Estado:** ✅ **Cumplido (MVP)**.
- **Evidencia:** `microservicio-notificaciones-go/cmd/server/main.go`, `microservicio-notificaciones-go/internal/consumer/low_stock_consumer.go`.

## 4) Capa de inventario “avanzada” del backend

- **Objetivo:** endpoints/casos de uso de inventario avanzado (movimientos, low-stock dedicado, dashboard y mayor separación hexagonal).
- **Estado actual:** ⚠️ **Parcial / Pendiente**.
- **Siguiente paso recomendado:**
  1. Crear módulo de inventario (dominio + aplicación + adaptadores).
  2. Añadir endpoints `POST /api/v1/inventory/movements` y `GET /api/v1/inventory/alerts/low-stock`.
  3. Incorporar endpoint de `dashboard summary`.
  4. Publicar eventos de dominio (`StockBelowThreshold`) hacia RabbitMQ.

## Resumen de avance

- Requisitos cumplidos en este corte inicial: **3 de 4**.
- Requisito pendiente principal: **inventario avanzado del backend**.
