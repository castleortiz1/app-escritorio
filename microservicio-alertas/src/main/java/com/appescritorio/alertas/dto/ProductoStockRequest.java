package com.appescritorio.alertas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoStockRequest(
        @NotNull Long id,
        @NotBlank String nombre,
        @NotNull @Min(0) Integer stockActual,
        @NotNull @Min(0) Integer stockMinimo
) {
}
