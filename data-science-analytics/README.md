# Data Science y Analytics Service

Microservicio inicial para cubrir capacidades de **Data Science y Analytics** del sistema.

## Capacidades incluidas

- Predicción de demanda por producto y horizonte.
- Detección de productos de baja rotación (*slow movers*).
- Recomendación básica de reorden.

## Stack

- Python 3.11+
- FastAPI
- Pydantic

## Ejecutar localmente

```bash
cd data-science-analytics
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8090
```

## Endpoints

- `GET /health`
- `POST /api/v1/predictions/demand`
- `POST /api/v1/insights/slow-movers`
- `POST /api/v1/insights/demand-prioritization`
- `POST /api/v1/recommendations/reorder`

## Ejecutar pruebas

```bash
cd data-science-analytics
pytest
```
