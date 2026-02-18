# Microservicio de Notificaciones (Go)

Implementación inicial del microservicio orientado a eventos.

## Capacidades iniciales

- `GET /health` para monitoreo.
- Consumidor de eventos `stock.low` desde RabbitMQ.
- Logging simple de eventos recibidos.

## Ejecutar

```bash
cd microservicio-notificaciones-go
go mod tidy
go run ./cmd/server
```

Variables:

- `RABBITMQ_URL` (default `amqp://app:app@localhost:5672/`)
- `RABBITMQ_QUEUE` (default `stock.low`)
- `PORT` (default `8082`)
