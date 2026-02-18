# app-escritorio

Repositorio principal del sistema **app-escritorio**, organizado como monorepo con servicios independientes.

## Componentes

- `backend/`: API principal (Spring Boot + Maven + JPA + seguridad JWT).
- `microservicio-alertas/`: microservicio de alertas de inventario (Spring Boot + Maven).
- `data-science-analytics/`: servicio de predicción e insights (FastAPI + Python).
- `docs/`: documentación de arquitectura y propuesta técnica.

## Documentación relacionada

- [Sistema de Inventario para Pequeños Negocios](docs/arquitectura-sistema-inventario.md)
- [README del backend](backend/README.md)
- [README de Data Science y Analytics](data-science-analytics/README.md)
- [README de microservicio de alertas](microservicio-alertas/README.md)

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

## Ejecución de pruebas

```bash
cd backend && mvn test
cd ../microservicio-alertas && mvn test
cd ../data-science-analytics && pytest
```
