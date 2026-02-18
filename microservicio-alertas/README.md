# Microservicio de Alertas (Spring Boot)

Microservicio independiente para evaluación de inventario y generación de alertas de stock bajo.

## Stack

- Java 17
- Spring Boot 3
- Maven

## Funcionalidades

- Evalúa una lista de productos enviada por otro servicio.
- Genera alertas cuando el stock está por debajo del mínimo.
- Define severidad de alerta (`ALTA` o `CRITICA`).
- Expone endpoint de salud para monitoreo.

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

## Pruebas

```bash
cd microservicio-alertas
mvn test
```
