from datetime import date
from typing import List

from pydantic import BaseModel, Field


class SalesPoint(BaseModel):
    fecha: date
    unidades: int = Field(ge=0)


class DemandPredictionRequest(BaseModel):
    producto_id: str
    horizonte_dias: int = Field(default=14, ge=1, le=90)
    historial_ventas: List[SalesPoint] = Field(default_factory=list)


class DemandPredictionResponse(BaseModel):
    producto_id: str
    horizonte_dias: int
    demanda_estimada_total: int
    demanda_estimada_diaria: float


class SlowMoversRequest(BaseModel):
    minimo_rotacion: float = Field(default=0.15, ge=0.0, le=1.0)
    productos: List[DemandPredictionRequest] = Field(default_factory=list)


class SlowMoverItem(BaseModel):
    producto_id: str
    rotacion: float
    es_slow_mover: bool


class SlowMoversResponse(BaseModel):
    evaluados: int
    slow_movers: List[SlowMoverItem]


class ReorderRequest(BaseModel):
    producto_id: str
    stock_actual: int = Field(ge=0)
    lead_time_dias: int = Field(ge=1, le=90)
    safety_stock: int = Field(default=0, ge=0)
    historial_ventas: List[SalesPoint] = Field(default_factory=list)


class ReorderResponse(BaseModel):
    producto_id: str
    punto_reorden: int
    cantidad_recomendada: int
    requiere_reorden: bool
