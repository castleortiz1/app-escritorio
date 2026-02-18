# Data Science y Analytics Service

Microservicio para capacidades de analítica e inteligencia de inventario.

## Stack

- Python 3.11+
- FastAPI
- Pydantic
- Pytest

## Capacidades incluidas

- Predicción de demanda por producto y horizonte.
- Detección de productos de baja rotación (*slow movers*).
- Priorización de productos por demanda proyectada.
- Recomendación básica de reorden.

## Ejecutar localmente

```bash
cd data-science-analytics
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8090
```

Servicio disponible en `http://localhost:8090`.

## Endpoints

- `GET /health`
- `POST /api/v1/predictions/demand`
- `POST /api/v1/insights/slow-movers`
- `POST /api/v1/insights/demand-prioritization`
- `POST /api/v1/recommendations/reorder`

Documentación interactiva (Swagger UI):

- `http://localhost:8090/docs`

## Ejecutar pruebas

```bash
cd data-science-analytics
pytest
```
