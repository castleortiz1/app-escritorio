# app-escritorio

Documento de arquitectura y propuesta técnica:

- [Sistema de Inventario para Pequeños Negocios](docs/arquitectura-sistema-inventario.md)

## Backend (Spring Boot + Maven)

Se inicializó un backend en `backend/` con:

- Java 17
- Spring Boot 3
- Maven
- JPA + Spring Web
- Perfil `dev` usando H2 en memoria
- Perfil `prod` usando PostgreSQL

### Ejecutar en desarrollo (H2)

```bash
cd backend
mvn spring-boot:run
```

### Ejecutar en producción (PostgreSQL)

```bash
cd backend
SPRING_PROFILES_ACTIVE=prod DB_URL=jdbc:postgresql://localhost:5432/appescritorio DB_USER=postgres DB_PASSWORD=postgres mvn spring-boot:run
```


## Microservicio independiente: Alertas de Inventario

Se agregó un microservicio independiente en `microservicio-alertas/` (Spring Boot + Maven) que brinda:

- Evaluación de stock bajo.
- Generación de alertas con severidad (`ALTA` / `CRITICA`).
- Endpoint de salud para monitoreo.

### Ejecutar

```bash
cd microservicio-alertas
mvn spring-boot:run
```

> Corre por defecto en `http://localhost:8081`.
