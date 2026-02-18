package com.appescritorio.backend.service;

import com.appescritorio.backend.model.Venta;
import com.appescritorio.backend.repository.VentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Venta obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
    }

    public Venta actualizar(Long id, Venta venta) {
        Venta ventaExistente = obtenerPorId(id);
        ventaExistente.setFecha(venta.getFecha());
        ventaExistente.setTotal(venta.getTotal());
        return ventaRepository.save(ventaExistente);
    }

    public void eliminar(Long id) {
        Venta venta = obtenerPorId(id);
        ventaRepository.delete(venta);
    }
}
