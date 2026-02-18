# Análisis general de brechas del proyecto

## Estado general

El monorepo ya tiene una base funcional multi-servicio (backend Java, frontend React, analítica Python, alertas Java, notificaciones Go y RabbitMQ), pero sigue en etapa **MVP técnico**. La propia documentación de requisitos confirma que el pendiente principal es la capa de inventario avanzado del backend.

## Qué falta (priorizado)

### 1) Inventario avanzado del backend (prioridad crítica)

Falta implementar el bloque que hoy aparece como pendiente en la documentación del proyecto:

- Movimientos de inventario (entradas, salidas, ajustes).
- Endpoint dedicado de alertas de stock bajo.
- Endpoint de dashboard/resumen.
- Publicación de eventos de dominio (`StockBelowThreshold`) al bus.

Sin este bloque, el backend no cumple todavía el objetivo de inventario “avanzado” ni orquesta correctamente la arquitectura orientada a eventos.

## 2) Flujo real ventas → stock → eventos

Aunque existe CRUD de ventas y productos, no hay modelado de líneas de venta ni lógica transaccional para descontar stock por producto en el registro de una venta. En la práctica:

- `Venta` solo guarda `fecha` y `total`.
- No hay relación entre una venta y los productos vendidos.
- No se dispara ningún evento de negocio al persistir ventas.

Esto limita funciones clave de negocio (consistencia de stock, alertas automáticas y analítica confiable).

## 3) Frontend todavía es scaffold

La SPA existe, pero todavía es una pantalla inicial estática. Falta construir:

- Login y gestión de sesión JWT.
- Vistas CRUD conectadas al backend.
- Pantallas de inventario, alertas y dashboard.
- Manejo de errores, estados de carga y rutas protegidas.

## 4) Notificaciones Go incompleto para CI y producción

El servicio Go arranca y consume RabbitMQ, pero está en estado inicial:

- No incluye `go.sum` en el repositorio (afecta reproducibilidad/CI).
- No tiene pruebas unitarias o de integración.
- No persiste ni expone notificaciones (solo `GET /health`).
- No implementa deduplicación, reintentos ni políticas de entrega.

## 5) Testing y calidad transversal insuficientes

Actualmente hay pruebas mínimas por servicio. Faltan:

- Pruebas unitarias de dominio backend (más allá del smoke test base).
- Pruebas de integración de persistencia y seguridad.
- Pruebas del consumidor Go y reglas de notificación.
- Pruebas frontend (unitarias y E2E).
- Cobertura mínima y quality gates en CI.

## 6) DevOps/operación todavía básico

Falta infraestructura de entrega/operación para entorno real:

- No hay pipeline CI/CD en repositorio.
- No hay Dockerfiles para servicios.
- Solo existe `docker-compose` para RabbitMQ.
- No hay observabilidad (métricas, trazas, alertas) ni gestión formal de secretos.

## 7) Integración de servicios aún parcial

La arquitectura documentada propone integración por eventos y por APIs entre backend, notificaciones y analytics. Hoy esos enlaces están mayormente desacoplados o manuales.

## Plan sugerido (orden recomendado)

1. **Backend inventario avanzado + eventos** (movimientos, low-stock, dashboard, publisher RabbitMQ).
2. **Modelo de ventas con detalle de líneas** + descuento de stock transaccional.
3. **Frontend funcional mínimo** (auth + catálogo + inventario + alertas).
4. **Cerrar notificaciones Go** (go.sum, tests, endpoint de notificaciones, reglas básicas).
5. **CI/CD mínimo** (build, tests, lint por servicio).
6. **Endurecimiento operativo** (Dockerfiles, observabilidad, secretos, ambientes).

## Resultado esperado al cerrar estas brechas

Con estos puntos completados, el proyecto pasaría de MVP técnico a una versión operable de negocio, alineada con la arquitectura objetivo y lista para crecimiento incremental.
