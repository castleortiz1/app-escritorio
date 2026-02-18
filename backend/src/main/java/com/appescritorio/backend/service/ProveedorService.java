package com.appescritorio.backend.service;

import com.appescritorio.backend.model.Proveedor;
import com.appescritorio.backend.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor crear(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
    }

    public Proveedor actualizar(Long id, Proveedor proveedor) {
        Proveedor proveedorExistente = obtenerPorId(id);
        proveedorExistente.setNombre(proveedor.getNombre());
        proveedorExistente.setContacto(proveedor.getContacto());
        return proveedorRepository.save(proveedorExistente);
    }

    public void eliminar(Long id) {
        Proveedor proveedor = obtenerPorId(id);
        proveedorRepository.delete(proveedor);
    }
}
