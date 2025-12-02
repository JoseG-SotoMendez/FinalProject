package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.entity.Cliente;
import com.unicordoba.FinalProject.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> getAll() { return clienteService.obtenerTodos(); }

    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) { return clienteService.guardarCliente(cliente); }
}