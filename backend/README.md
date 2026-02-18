# Backend de app-escritorio

Este documento explica **paso a paso** cómo ejecutar el backend en Windows, Linux y macOS.

Está escrito para personas sin experiencia técnica, así que incluye:

- Qué instalar.
- Cómo encender el backend.
- Cómo saber si quedó funcionando.
- Cómo identificar si una falla es de **backend**, de **base de datos (DB)** o de **frontend**.

---

## 1) ¿Qué es el backend de este proyecto?

El backend es la parte que:

- recibe peticiones del frontend,
- valida reglas de negocio,
- guarda/lee información de base de datos,
- responde datos por API.

En este proyecto está construido con:

- Java 17,
- Spring Boot 3,
- Maven,
- base de datos H2 (desarrollo) o PostgreSQL (producción).

Por defecto corre en:

- `http://localhost:8080`

---

## 2) Requisitos previos

Instala estos componentes antes de ejecutar:

1. **Java 17**
2. **Maven 3.9+**

> Nota: En este repositorio no existe `mvnw` (Maven Wrapper), por eso se usa Maven instalado en el sistema (`mvn`).

### Verificar instalación

#### Windows (PowerShell)

```powershell
java -version
mvn -version
```

#### Linux / macOS (Terminal)

```bash
java -version
mvn -version
```

Si ambos comandos devuelven una versión, ya puedes continuar.

---

## 3) Arranque rápido (modo desarrollo, recomendado para iniciar)

En desarrollo se usa una base en memoria (H2), así que es la forma más fácil de probar.

### Windows (PowerShell)

```powershell
cd backend
mvn spring-boot:run
```

### Linux / macOS

```bash
cd backend
mvn spring-boot:run
```

### ¿Cómo sé que arrancó bien?

Debes ver en consola algo parecido a:

- `Started BackendApplication`
- y que no aparezcan errores en rojo después de ese mensaje.

Cuando está encendido, prueba en tu navegador:

- `http://localhost:8080/api/productos`

Si responde (aunque sea `[]`), backend levantó correctamente.

---

## 4) Perfil de base de datos

Este proyecto tiene dos perfiles:

### `dev` (por defecto)

- Usa H2 en memoria.
- No necesitas instalar PostgreSQL.
- Configuración activa por defecto en `application.yml`.

### `prod`

- Usa PostgreSQL.
- Requiere que la DB exista y acepte conexiones.

---

## 5) Ejecutar en producción local (PostgreSQL)

Antes de arrancar en `prod`, prepara PostgreSQL:

- servidor de PostgreSQL encendido,
- base de datos creada (ejemplo: `appescritorio`),
- usuario y contraseña válidos.

### Windows (PowerShell)

```powershell
cd backend
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://localhost:5432/appescritorio"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"
mvn spring-boot:run
```

### Windows (CMD)

```cmd
cd backend
set SPRING_PROFILES_ACTIVE=prod
set DB_URL=jdbc:postgresql://localhost:5432/appescritorio
set DB_USER=postgres
set DB_PASSWORD=postgres
mvn spring-boot:run
```

### Linux / macOS

```bash
cd backend
SPRING_PROFILES_ACTIVE=prod \
DB_URL=jdbc:postgresql://localhost:5432/appescritorio \
DB_USER=postgres \
DB_PASSWORD=postgres \
mvn spring-boot:run
```

---

## 6) APIs principales para validación rápida

Puedes usar estas rutas para confirmar que el backend responde:

- `GET /api/productos`
- `GET /api/categorias`
- `GET /api/clientes`
- `GET /api/proveedores`
- `GET /api/ventas`

Ejemplo rápido:

```bash
curl http://localhost:8080/api/productos
```

---

## 7) Diagnóstico de fallas para personas no técnicas

Si algo falla, esta sección te ayuda a identificar **dónde** está el problema.

### Regla simple de diagnóstico

1. **¿El backend arranca?**
   - Si no arranca: problema de backend o DB.
2. **¿La URL API responde?** (`http://localhost:8080/api/productos`)
   - Si no responde: backend caído o puerto incorrecto.
3. **¿La API responde pero la pantalla no muestra datos?**
   - Probablemente problema de frontend (consumo de API, renderizado o CORS/configuración cliente).

---

## 8) Tabla de síntomas: backend vs DB vs frontend

| Síntoma | Causa más probable | Capa afectada | Qué hacer |
|---|---|---|---|
| No abre `http://localhost:8080/api/productos` | El backend no está corriendo o puerto ocupado | Backend | Revisa consola del `mvn spring-boot:run`, corrige error y reinicia |
| Error de conexión a DB en consola (`Connection refused`, `password authentication failed`) | Credenciales/servidor PostgreSQL incorrectos | DB | Verifica `DB_URL`, `DB_USER`, `DB_PASSWORD` y que PostgreSQL esté encendido |
| Backend inicia, pero al guardar/listar da error SQL | Esquema/tablas no compatibles o datos inválidos | DB + Backend | Revisar mensaje SQL exacto y estructura de tablas |
| Backend responde JSON en navegador/curl, pero la pantalla sigue vacía | Frontend no está consumiendo bien la API | Frontend | Revisar configuración de URL base y errores en consola del navegador |
| Mensajes tipo `404` en endpoints conocidos | Ruta mal escrita en frontend o cliente | Frontend (o integración) | Comparar URL llamada con rutas `/api/...` |
| Mensajes tipo `500` al llamar API | Excepción interna del backend | Backend | Revisar stacktrace en consola del backend |

---

## 9) Checklist de soporte (paso a paso)

Cuando un usuario reporte un error, validar en este orden:

1. ¿Backend encendido?
   - Debe haber una terminal con `mvn spring-boot:run` ejecutándose sin errores fatales.
2. ¿Puerto 8080 disponible?
   - Si otro proceso lo usa, Spring Boot no podrá iniciar.
3. ¿Endpoint básico responde?
   - Probar `http://localhost:8080/api/productos`.
4. Si está en `prod`, ¿PostgreSQL está activo y accesible?
   - Confirmar host/puerto/usuario/contraseña.
5. Si API responde pero UI falla:
   - problema probablemente de frontend.

---

## 10) Errores comunes y solución

### Error: `mvn: command not found` / `mvn no se reconoce`

**Qué significa**: Maven no está instalado o no está en PATH.

**Solución**:

- Instalar Maven.
- Cerrar y abrir terminal.
- Verificar con `mvn -version`.

### Error: `java: command not found`

**Qué significa**: Java no está instalado o no está en PATH.

**Solución**:

- Instalar JDK 17.
- Configurar variable `JAVA_HOME`.
- Verificar con `java -version`.

### Error: `Port 8080 was already in use`

**Qué significa**: otro programa está usando ese puerto.

**Solución**:

- Cerrar el proceso que usa 8080, o
- cambiar puerto en configuración de Spring (`server.port`).

### Error de PostgreSQL en `prod`

**Qué significa**: backend no puede conectarse a DB.

**Solución**:

- revisar datos de conexión,
- confirmar que la DB existe,
- confirmar que el usuario tiene permisos.

---

## 11) Apagado del backend

Para detenerlo, presiona:

- `Ctrl + C` en la terminal donde está corriendo.

---

## 12) Resumen para usuarios no técnicos

Si tu pantalla falla, usa esta regla:

- **Si no responde `localhost:8080`** → normalmente es backend.
- **Si backend muestra errores de conexión SQL** → normalmente es base de datos.
- **Si backend responde datos pero la pantalla no los muestra** → normalmente es frontend.

Con esta guía puedes reportar incidencias con mayor precisión y reducir tiempos de soporte.
