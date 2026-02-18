# Microservicio de Alertas (Spring Boot)

Este microservicio forma parte de **app-escritorio** y está desarrollado en:

- **Java 17**
- **Spring Boot 3**
- **Maven**

Su objetivo es evaluar niveles de inventario y generar alertas de stock bajo para que el backend principal pueda tomar acciones.

## Funcionalidades

- Evalúa una lista de productos enviada por el backend principal.
- Genera alertas cuando el stock está por debajo del mínimo configurado.
- Devuelve severidad de alerta (`ALTA` o `CRITICA`).
- Expone un endpoint de salud para monitoreo.

## Endpoints

- `GET /api/alertas/health`
- `POST /api/alertas/stock/evaluar`

### Ejemplo de payload

```json
{
  "productos": [
    { "id": 1, "nombre": "Laptop", "stockActual": 2, "stockMinimo": 5 },
    { "id": 2, "nombre": "Mouse", "stockActual": 0, "stockMinimo": 2 }
  ]
}
```

## Ejecución local

```bash
cd microservicio-alertas
mvn spring-boot:run
```

Por defecto corre en `http://localhost:8081`.

## Nota

Tanto el backend principal (`backend/`) como este microservicio (`microservicio-alertas/`) están implementados con **Java + Spring Boot**.
