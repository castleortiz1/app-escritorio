# Mensajería / Event Bus (RabbitMQ)

Implementación inicial del bus de eventos para el proyecto.

## Ejecutar

```bash
cd event-bus
docker compose up -d
```

## Accesos

- AMQP: `amqp://app:app@localhost:5672/`
- UI de administración: `http://localhost:15672` (usuario: `app`, clave: `app`)
