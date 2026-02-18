package com.appescritorio.backend.service;

import com.appescritorio.backend.model.Venta;
import com.appescritorio.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta crear(Venta venta) {
        return ventaRepository.save(venta);
    }
}
