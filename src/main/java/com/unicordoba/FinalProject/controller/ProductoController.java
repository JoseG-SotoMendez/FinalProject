package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.entity.Producto;
import com.unicordoba.FinalProject.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAll() { return productoService.obtenerTodos(); }

    @PostMapping
    public Producto create(@RequestBody Producto producto) { return productoService.guardarProducto(producto); }
}