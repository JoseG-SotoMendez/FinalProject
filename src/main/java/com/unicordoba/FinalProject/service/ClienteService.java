package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.entity.Cliente;
import com.unicordoba.FinalProject.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodos() { return clienteRepository.findAll(); }

    public Optional<Cliente> obtenerPorId(Integer id) { return clienteRepository.findById(id); }

    public Cliente guardarCliente(Cliente cliente) { return clienteRepository.save(cliente); }

    public void eliminarCliente(Integer id) { clienteRepository.deleteById(id); }
}