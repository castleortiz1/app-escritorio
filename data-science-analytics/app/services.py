from __future__ import annotations

from statistics import mean

from .schemas import (
    DemandPredictionRequest,
    DemandPredictionResponse,
    ReorderRequest,
    ReorderResponse,
    SlowMoverItem,
    SlowMoversRequest,
    SlowMoversResponse,
)


def _daily_average(request: DemandPredictionRequest) -> float:
    if not request.historial_ventas:
        return 0.0
    return mean(point.unidades for point in request.historial_ventas)


def predict_demand(request: DemandPredictionRequest) -> DemandPredictionResponse:
    demanda_diaria = _daily_average(request)
    total = round(demanda_diaria * request.horizonte_dias)
    return DemandPredictionResponse(
        producto_id=request.producto_id,
        horizonte_dias=request.horizonte_dias,
        demanda_estimada_total=max(total, 0),
        demanda_estimada_diaria=round(demanda_diaria, 2),
    )


def detect_slow_movers(request: SlowMoversRequest) -> SlowMoversResponse:
    items: list[SlowMoverItem] = []
    for producto in request.productos:
        promedio = _daily_average(producto)
        rotacion = round(promedio / 10, 2)
        items.append(
            SlowMoverItem(
                producto_id=producto.producto_id,
                rotacion=rotacion,
                es_slow_mover=rotacion < request.minimo_rotacion,
            )
        )

    return SlowMoversResponse(
        evaluados=len(request.productos),
        slow_movers=[item for item in items if item.es_slow_mover],
    )


def recommend_reorder(request: ReorderRequest) -> ReorderResponse:
    demanda_diaria = mean(point.unidades for point in request.historial_ventas) if request.historial_ventas else 0
    punto_reorden = round((demanda_diaria * request.lead_time_dias) + request.safety_stock)
    requiere = request.stock_actual <= punto_reorden
    recomendado = max(punto_reorden - request.stock_actual, 0) if requiere else 0

    return ReorderResponse(
        producto_id=request.producto_id,
        punto_reorden=max(punto_reorden, 0),
        cantidad_recomendada=recomendado,
        requiere_reorden=requiere,
    )
