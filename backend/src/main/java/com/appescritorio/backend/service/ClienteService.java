package com.appescritorio.backend.service;

import com.appescritorio.backend.model.Cliente;
import com.appescritorio.backend.repository.ClienteRepository;
import org.springframework.stereotype.Service;

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
}
