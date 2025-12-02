package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.dto.DetallePedidoDTO;
import com.unicordoba.FinalProject.dto.PedidoDTO;
import com.unicordoba.FinalProject.entity.*;
import com.unicordoba.FinalProject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private SedeRepository sedeRepository;
    @Autowired private InventarioRepository inventarioRepository;

    @Transactional
    public Pedido crearPedido(PedidoDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Sede sede = sedeRepository.findById(dto.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setSede(sede);
        pedido.setTipo(dto.getTipo());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);

        pedido = pedidoRepository.save(pedido);

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (DetallePedidoDTO itemDto : dto.getItems()) {
            Producto producto = productoRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Inventario inventario = inventarioRepository.findBySedeAndProducto(sede, producto)
                    .orElseThrow(() -> new RuntimeException("El producto " + producto.getNombre() + " no está registrado en el inventario de esta sede."));

            BigDecimal cantidadSolicitada = new BigDecimal(itemDto.getCantidad());

            if (inventario.getCantidad().compareTo(cantidadSolicitada) < 0) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() + ". Disponible: " + inventario.getCantidad());
            }

            inventario.setCantidad(inventario.getCantidad().subtract(cantidadSolicitada));
            inventarioRepository.save(inventario);


            OrderItem item = new OrderItem();
            item.setPedido(pedido);
            item.setProducto(producto);
            item.setCantidad(itemDto.getCantidad());
            item.setPrecioUnitario(producto.getPrecioBase());
            item.setObservaciones(itemDto.getObservaciones());
            item.setSubtotal(producto.getPrecioBase().multiply(cantidadSolicitada));

            orderItemRepository.save(item);
            totalPedido = totalPedido.add(item.getSubtotal());
        }

        pedido.setTotal(totalPedido);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }
}