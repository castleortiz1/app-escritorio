from fastapi import FastAPI

from .schemas import (
    DemandPredictionRequest,
    DemandPredictionResponse,
    ReorderRequest,
    ReorderResponse,
    SlowMoversRequest,
    SlowMoversResponse,
)
from .services import detect_slow_movers, predict_demand, recommend_reorder

app = FastAPI(
    title="Data Science y Analytics Service",
    version="0.1.0",
    description="Servicio inicial para predicciones de demanda e insights de inventario.",
)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.post("/api/v1/predictions/demand", response_model=DemandPredictionResponse)
def demand_prediction(payload: DemandPredictionRequest) -> DemandPredictionResponse:
    return predict_demand(payload)


@app.post("/api/v1/insights/slow-movers", response_model=SlowMoversResponse)
def slow_movers(payload: SlowMoversRequest) -> SlowMoversResponse:
    return detect_slow_movers(payload)


@app.post("/api/v1/recommendations/reorder", response_model=ReorderResponse)
def reorder(payload: ReorderRequest) -> ReorderResponse:
    return recommend_reorder(payload)
