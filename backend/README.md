# Backend de app-escritorio

API principal del sistema, construida con **Java 17 + Spring Boot 3 + Maven**.

## Capacidades actuales

- CRUD base para entidades de negocio:
  - `productos`
  - `categorias`
  - `clientes`
  - `proveedores`
  - `ventas`
- Seguridad con autenticación JWT:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
  - `POST /api/auth/refresh`
- Límite de intentos para login y configuración CORS vía variables de entorno.

## Requisitos

- Java 17
- Maven 3.9+

Verificación rápida:

```bash
java -version
mvn -version
```

## Ejecución local

### Desarrollo (`dev`, por defecto, con H2 en memoria)

```bash
cd backend
mvn spring-boot:run
```

### Producción local (`prod`, con PostgreSQL)

```bash
cd backend
SPRING_PROFILES_ACTIVE=prod \
DB_URL=jdbc:postgresql://localhost:5432/appescritorio \
DB_USER=postgres \
DB_PASSWORD=postgres \
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Variables de entorno de seguridad (recomendado)

Si no se definen, se usan valores por defecto (cuando existen) y algunas rutas pueden no estar aptas para entornos reales.

- `APP_ADMIN_USERNAME`
- `APP_ADMIN_PASSWORD`
- `APP_JWT_ACCESS_SECRET`
- `APP_JWT_REFRESH_SECRET`
- `APP_JWT_ACCESS_EXPIRATION_MS` (default: `900000`)
- `APP_JWT_REFRESH_EXPIRATION_MS` (default: `604800000`)
- `APP_CORS_ALLOWED_ORIGINS` (default: `http://localhost:3000`)
- `APP_CORS_ALLOWED_METHODS` (default: `GET,POST,PUT,DELETE,OPTIONS`)
- `APP_CORS_ALLOWED_HEADERS` (default: `Authorization,Content-Type`)
- `APP_LOGIN_MAX_ATTEMPTS` (default: `5`)
- `APP_LOGIN_WINDOW_MS` (default: `60000`)

## Endpoints de validación rápida

- `GET /api/productos`
- `GET /api/categorias`
- `GET /api/clientes`
- `GET /api/proveedores`
- `GET /api/ventas`
- `POST /api/auth/login`

Ejemplo:

```bash
curl http://localhost:8080/api/productos
```

## Pruebas

```bash
cd backend
mvn test
```
