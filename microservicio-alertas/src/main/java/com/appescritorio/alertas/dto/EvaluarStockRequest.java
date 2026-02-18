package com.appescritorio.alertas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record EvaluarStockRequest(
        @NotEmpty List<@Valid ProductoStockRequest> productos
) {
}
