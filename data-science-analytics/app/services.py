from __future__ import annotations

from statistics import mean

from .schemas import (
    DemandPredictionRequest,
    DemandPredictionResponse,
    DemandPrioritizationRequest,
    DemandPrioritizationResponse,
    DemandSegment,
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


def prioritize_products_by_demand(
    request: DemandPrioritizationRequest,
) -> DemandPrioritizationResponse:
    products_with_demand = sorted(
        [
            (producto, round(_daily_average(producto), 2))
            for producto in request.productos
        ],
        key=lambda item: item[1],
        reverse=True,
    )

    top_count = min(request.top_n_mas_vendidos, len(products_with_demand))
    top_products = products_with_demand[:top_count]
    remaining_products = products_with_demand[top_count:]

    lower_threshold = 0.0
    if remaining_products:
        demandas_remanentes = sorted(demanda for _, demanda in remaining_products)
        index_baja = max(round((len(demandas_remanentes) - 1) * 0.33), 0)
        lower_threshold = demandas_remanentes[index_baja]

    intermedia: list[DemandSegment] = []
    baja: list[DemandSegment] = []

    for producto, demanda in remaining_products:
        is_low_demand = demanda <= lower_threshold
        target = baja if is_low_demand else intermedia
        clasificacion = "baja_demanda" if is_low_demand else "demanda_intermedia"
        sugerencia = "sugerir_ofertas_o_liquidacion" if is_low_demand else "venta_normal"
        target.append(
            DemandSegment(
                producto_id=producto.producto_id,
                demanda_estimada_diaria=demanda,
                clasificacion=clasificacion,
                sugerencia=sugerencia,
            )
        )

    top = [
        DemandSegment(
            producto_id=producto.producto_id,
            demanda_estimada_diaria=demanda,
            clasificacion="alta_demanda",
            sugerencia="mostrar_inicialmente",
        )
        for producto, demanda in top_products
    ]

    return DemandPrioritizationResponse(
        total_productos=len(products_with_demand),
        top_mas_vendidos=top,
        demanda_intermedia=intermedia,
        baja_demanda_ofertas_liquidacion=baja,
    )
