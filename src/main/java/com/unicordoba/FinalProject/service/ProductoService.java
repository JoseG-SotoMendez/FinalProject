package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.entity.Producto;
import com.unicordoba.FinalProject.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() { return productoRepository.findAll(); }

    public Optional<Producto> obtenerPorId(Integer id) { return productoRepository.findById(id); }

    public Producto guardarProducto(Producto producto) { return productoRepository.save(producto); }

    public void eliminarProducto(Integer id) { productoRepository.deleteById(id); }
}