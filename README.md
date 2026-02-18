# app-escritorio

Repositorio principal del sistema **app-escritorio**, organizado como monorepo con servicios independientes.

## Componentes

- `backend/`: API principal (Spring Boot + Maven + JPA + seguridad JWT).
- `microservicio-alertas/`: microservicio de alertas de inventario (Spring Boot + Maven).
- `data-science-analytics/`: servicio de predicción e insights (FastAPI + Python).
- `docs/`: documentación de arquitectura y propuesta técnica.
- `frontend-spa/`: frontend inicial (React + TypeScript + Vite).
- `event-bus/`: infraestructura de mensajería (RabbitMQ).
- `microservicio-notificaciones-go/`: microservicio de notificaciones orientado a eventos.

## Documentación relacionada

- [Sistema de Inventario para Pequeños Negocios](docs/arquitectura-sistema-inventario.md)
- [README del backend](backend/README.md)
- [README de Data Science y Analytics](data-science-analytics/README.md)
- [README de microservicio de alertas](microservicio-alertas/README.md)
- [README del frontend SPA](frontend-spa/README.md)
- [README del event bus](event-bus/README.md)
- [README de notificaciones Go](microservicio-notificaciones-go/README.md)
- [Estado de requisitos iniciales](docs/requisitos-proyecto-inicial.md)

## Arranque rápido de cada servicio

### Backend

```bash
cd backend
mvn spring-boot:run
```

Disponible por defecto en `http://localhost:8080`.

### Microservicio de alertas

```bash
cd microservicio-alertas
mvn spring-boot:run
```

Disponible por defecto en `http://localhost:8081`.

### Data Science y Analytics

```bash
cd data-science-analytics
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8090
```

Disponible por defecto en `http://localhost:8090`.

### Frontend SPA

```bash
cd frontend-spa
npm install
npm run dev
```

Disponible por defecto en `http://localhost:3000`.

### Event Bus (RabbitMQ)

```bash
cd event-bus
docker compose up -d
```

RabbitMQ disponible en `amqp://app:app@localhost:5672/` y panel en `http://localhost:15672`.

### Microservicio de notificaciones (Go)

```bash
cd microservicio-notificaciones-go
go run ./cmd/server
```

Disponible por defecto en `http://localhost:8082`.

## Ejecución de pruebas

```bash
cd backend && mvn test
cd ../microservicio-alertas && mvn test
cd ../data-science-analytics && pytest
```
