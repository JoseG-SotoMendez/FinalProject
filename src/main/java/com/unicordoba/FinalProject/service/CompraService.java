package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.dto.CompraDTO;
import com.unicordoba.FinalProject.dto.DetalleCompraDTO;
import com.unicordoba.FinalProject.entity.*;
import com.unicordoba.FinalProject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CompraService {

    @Autowired private CompraRepository compraRepository;
    @Autowired private PurchaseItemRepository purchaseItemRepository;
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private SedeRepository sedeRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private InventarioRepository inventarioRepository;

    @Transactional
    public Compra registrarCompra(CompraDTO dto) {
        // 1. Validar Proveedor y Sede
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Sede sede = sedeRepository.findById(dto.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        // 2. Crear Encabezado de Compra
        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setSede(sede);
        compra.setFecha(LocalDateTime.now());
        compra.setEstado("RECIBIDO");
        compra.setTotal(BigDecimal.ZERO);

        compra = compraRepository.save(compra);

        BigDecimal totalCompra = BigDecimal.ZERO;

        // 3. Procesar Items y SUMAR AL INVENTARIO
        for (DetalleCompraDTO itemDto : dto.getItems()) {
            Producto producto = productoRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + itemDto.getProductoId()));

            // --- LÓGICA DE INVENTARIO (SUMAR) ---
            // Buscamos si ya existe inventario de este producto en esta sede
            Inventario inventario = inventarioRepository.findBySedeAndProducto(sede, producto)
                    .orElse(null);

            if (inventario == null) {
                // ¡Caso Nuevo! Si no existe, lo creamos desde cero
                inventario = new Inventario();
                inventario.setSede(sede);
                inventario.setProducto(producto);
                inventario.setCantidad(BigDecimal.ZERO);
                inventario.setUnidad("UNIDAD");
            }

            // Sumamos la cantidad comprada al stock actual
            inventario.setCantidad(inventario.getCantidad().add(itemDto.getCantidad()));
            inventarioRepository.save(inventario);
            // -------------------------------------

            // Guardar detalle de compra
            PurchaseItem item = new PurchaseItem();
            item.setCompra(compra);
            item.setProducto(producto);
            item.setCantidad(itemDto.getCantidad());
            item.setPrecioUnitario(itemDto.getCostoUnitario());

            // Subtotal = Cantidad * Costo
            BigDecimal subtotal = itemDto.getCantidad().multiply(itemDto.getCostoUnitario());
            item.setSubtotal(subtotal);

            purchaseItemRepository.save(item);
            totalCompra = totalCompra.add(subtotal);
        }

        // 4. Actualizar total final
        compra.setTotal(totalCompra);
        return compraRepository.save(compra);
    }
}