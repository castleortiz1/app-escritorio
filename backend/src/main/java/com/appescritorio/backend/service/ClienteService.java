package com.appescritorio.backend.service;

import com.appescritorio.backend.model.Cliente;
import com.appescritorio.backend.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
    }

    public Cliente actualizar(Long id, Cliente cliente) {
        Cliente clienteExistente = obtenerPorId(id);
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setEmail(cliente.getEmail());
        return clienteRepository.save(clienteExistente);
    }

    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        clienteRepository.delete(cliente);
    }
}
