from datetime import date

from app.schemas import (
    DemandPredictionRequest,
    DemandPrioritizationRequest,
    ReorderRequest,
    SalesPoint,
    SlowMoversRequest,
)
from app.services import (
    detect_slow_movers,
    predict_demand,
    prioritize_products_by_demand,
    recommend_reorder,
)


def test_predict_demand_estimates_total() -> None:
    request = DemandPredictionRequest(
        producto_id="SKU-1",
        horizonte_dias=7,
        historial_ventas=[
            SalesPoint(fecha=date(2026, 1, 1), unidades=10),
            SalesPoint(fecha=date(2026, 1, 2), unidades=14),
        ],
    )

    response = predict_demand(request)

    assert response.demanda_estimada_diaria == 12
    assert response.demanda_estimada_total == 84


def test_detect_slow_movers_filters_by_threshold() -> None:
    request = SlowMoversRequest(
        minimo_rotacion=0.2,
        productos=[
            DemandPredictionRequest(
                producto_id="A",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=1)],
            ),
            DemandPredictionRequest(
                producto_id="B",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=5)],
            ),
        ],
    )

    response = detect_slow_movers(request)

    assert response.evaluados == 2
    assert len(response.slow_movers) == 1
    assert response.slow_movers[0].producto_id == "A"


def test_reorder_recommendation_when_below_threshold() -> None:
    request = ReorderRequest(
        producto_id="SKU-1",
        stock_actual=8,
        lead_time_dias=3,
        safety_stock=4,
        historial_ventas=[
            SalesPoint(fecha=date(2026, 1, 1), unidades=4),
            SalesPoint(fecha=date(2026, 1, 2), unidades=6),
        ],
    )

    response = recommend_reorder(request)

    assert response.requiere_reorden is True
    assert response.punto_reorden == 19
    assert response.cantidad_recomendada == 11


def test_prioritize_products_by_demand_segments_products() -> None:
    request = {
        "top_n_mas_vendidos": 2,
        "productos": [
            DemandPredictionRequest(
                producto_id="P1",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=12)],
            ),
            DemandPredictionRequest(
                producto_id="P2",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=9)],
            ),
            DemandPredictionRequest(
                producto_id="P3",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=3)],
            ),
            DemandPredictionRequest(
                producto_id="P4",
                historial_ventas=[SalesPoint(fecha=date(2026, 1, 1), unidades=0)],
            ),
        ],
    }

    response = prioritize_products_by_demand(DemandPrioritizationRequest.model_validate(request))

    assert response.total_productos == 4
    assert [item.producto_id for item in response.top_mas_vendidos] == ["P1", "P2"]
    assert [item.producto_id for item in response.demanda_intermedia] == ["P3"]
    assert [item.producto_id for item in response.baja_demanda_ofertas_liquidacion] == ["P4"]
