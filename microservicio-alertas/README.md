# microservicio-alertas

Microservicio independiente para evaluar niveles de inventario y generar alertas de stock bajo.

## Servicio que brinda al proyecto

- Evalúa una lista de productos enviada por el backend principal.
- Devuelve alertas con severidad (`ALTA` o `CRITICA`) para productos por debajo del stock mínimo.
- Expone un endpoint de salud para monitoreo.

## Endpoints

- `GET /api/alertas/health`
- `POST /api/alertas/stock/evaluar`

Ejemplo de payload para evaluación:

```json
{
  "productos": [
    { "id": 1, "nombre": "Laptop", "stockActual": 2, "stockMinimo": 5 },
    { "id": 2, "nombre": "Mouse", "stockActual": 0, "stockMinimo": 2 }
  ]
}
```

## Ejecutar

```bash
cd microservicio-alertas
mvn spring-boot:run
```

Por defecto corre en el puerto `8081`.
