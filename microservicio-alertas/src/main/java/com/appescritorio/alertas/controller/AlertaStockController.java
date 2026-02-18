package com.appescritorio.alertas.controller;

import com.appescritorio.alertas.dto.AlertaStockResponse;
import com.appescritorio.alertas.dto.EvaluarStockRequest;
import com.appescritorio.alertas.service.AlertaStockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
public class AlertaStockController {

    private final AlertaStockService alertaStockService;

    public AlertaStockController(AlertaStockService alertaStockService) {
        this.alertaStockService = alertaStockService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("service", "microservicio-alertas");
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stock/evaluar")
    public ResponseEntity<List<AlertaStockResponse>> evaluarStock(@Valid @RequestBody EvaluarStockRequest request) {
        List<AlertaStockResponse> alertas = alertaStockService.generarAlertas(request.productos());
        return ResponseEntity.ok(alertas);
    }
}
