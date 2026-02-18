package com.appescritorio.alertas.service;

import com.appescritorio.alertas.dto.AlertaStockResponse;
import com.appescritorio.alertas.dto.ProductoStockRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaStockService {

    public List<AlertaStockResponse> generarAlertas(List<ProductoStockRequest> productos) {
        return productos.stream()
                .filter(producto -> producto.stockActual() <= producto.stockMinimo())
                .map(this::aAlerta)
                .toList();
    }

    private AlertaStockResponse aAlerta(ProductoStockRequest producto) {
        String severidad = producto.stockActual() == 0 ? "CRITICA" : "ALTA";
        String mensaje = String.format(
                "El producto '%s' tiene stock %d y el mínimo permitido es %d",
                producto.nombre(), producto.stockActual(), producto.stockMinimo());

        return new AlertaStockResponse(
                producto.id(),
                producto.nombre(),
                producto.stockActual(),
                producto.stockMinimo(),
                severidad,
                mensaje
        );
    }
}
