package com.appescritorio.alertas.dto;

public record AlertaStockResponse(
        Long productoId,
        String producto,
        Integer stockActual,
        Integer stockMinimo,
        String severidad,
        String mensaje
) {
}
