package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.dto.DashboardDTO;
import com.unicordoba.FinalProject.entity.*;
import com.unicordoba.FinalProject.repository.*;
import com.unicordoba.FinalProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller // Esto indica que devolvemos Vistas HTML
public class WebController {

    // --- SERVICIOS ---
    @Autowired private ProductoService productoService;
    @Autowired private ClienteService clienteService;
    @Autowired private SedeService sedeService;
    @Autowired private CompraService compraService;

    // --- REPOSITORIOS (Para listar directamente) ---
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private SedeRepository sedeRepository;
    @Autowired private FacturaRepository facturaRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private PagoRepository pagoRepository;

    @Autowired private com.unicordoba.FinalProject.repository.ProveedorRepository proveedorRepository;

    // ================= INICIO Y DASHBOARD =================
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("titulo", "Sistema POS - Unicórdoba");
        return "index";
    }

    @GetMapping("/web/dashboard")
    public String dashboard(Model model) {
        DashboardDTO stats = new DashboardDTO();
        stats.setTotalVentasDia(pedidoRepository.contarVentasHoy());
        stats.setIngresosDia(pedidoRepository.sumarIngresosHoy());
        stats.setProductosBajoStock(inventarioRepository.contarProductosBajoStock());
        stats.setClientesRegistrados(clienteRepository.count());
        model.addAttribute("stats", stats);
        return "dashboard";
    }

    @GetMapping("/web/compras/nueva")
    public String formNuevaCompra(Model model) {
        model.addAttribute("listaProveedores", proveedorRepository.findAll());
        model.addAttribute("listaSedes", sedeService.obtenerTodas());
        model.addAttribute("listaProductos", productoService.obtenerTodos());
        return "nueva_compra"; // Vamos a crear este archivo ahora
    }

    // ================= 1. PRODUCTOS (Editar y Eliminar) =================
    @GetMapping("/web/productos")
    public String listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal minPrecio,
            @RequestParam(required = false) BigDecimal maxPrecio,
            @RequestParam(required = false) Boolean activo,
            Model model) {

        List<Producto> resultados;
        if (nombre == null && minPrecio == null && maxPrecio == null && activo == null) {
            resultados = productoService.obtenerTodos();
        } else {
            resultados = productoRepository.buscarProductosAvanzado(nombre, minPrecio, maxPrecio, activo);
        }
        model.addAttribute("listaProductos", resultados);
        return "productos";
    }

    @GetMapping("/web/productos/nuevo")
    public String formProducto(Model model) {
        Producto p = new Producto();
        p.setActivo(true);
        model.addAttribute("producto", p);
        return "nuevo_producto";
    }

    // EDITAR PRODUCTO: Cargamos el producto por ID y reutilizamos el formulario
    @GetMapping("/web/productos/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto p = productoService.obtenerPorId(id).orElse(null);
        model.addAttribute("producto", p);
        return "nuevo_producto";
    }

    // ELIMINAR PRODUCTO
    @GetMapping("/web/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return "redirect:/web/productos";
    }

    @PostMapping("/web/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoService.guardarProducto(producto);
        return "redirect:/web/productos";
    }

    // ==========================================
    // SECCIÓN 2: CLIENTES
    // ==========================================
    @GetMapping("/web/clientes")
    public String listarClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            Model model) {

        List<Cliente> lista;
        if (nombre == null && email == null && telefono == null) {
            lista = clienteService.obtenerTodos();
        } else {
            // Usamos el repositorio directamente para filtrar (necesitas inyectar clienteRepository arriba si usas el servicio solo para guardar)
            // Ojo: Para simplificar, llama al repositorio aquí, o crea el método en el servicio.
            // Asumiré que inyectas el repositorio o agregas el método en ClienteService.
            lista = clienteRepository.buscarConFiltros(nombre, email, telefono);
        }

        model.addAttribute("listaClientes", lista);
        return "clientes";
    }

    @GetMapping("/web/clientes/editar/{id}")
    public String editarCliente(@PathVariable Integer id, Model model) {
        Cliente c = clienteService.obtenerPorId(id).orElse(null);
        model.addAttribute("cliente", c);
        return "nuevo_cliente";
    }

    @GetMapping("/web/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return "redirect:/web/clientes";
    }

    @PostMapping("/web/clientes/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.guardarCliente(cliente);
        return "redirect:/web/clientes";
    }

    // ==========================================
    // SECCIÓN 3: SEDES
    // ==========================================
    @GetMapping("/web/sedes")
    public String listarSedes(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String nombre,
            Model model) {

        List<Sede> lista;
        if (ciudad == null && nombre == null) {
            lista = sedeService.obtenerTodas();
        } else {
            lista = sedeRepository.buscarPorCiudadYNombre(ciudad, nombre);
        }
        model.addAttribute("listaSedes", lista);
        return "sedes";
    }

    @GetMapping("/web/sedes/nuevo")
    public String formSede(Model model) {
        model.addAttribute("sede", new Sede());
        return "nueva_sede";
    }

    @GetMapping("/web/sedes/editar/{id}")
    public String editarSede(@PathVariable Integer id, Model model) {
        Sede s = sedeService.obtenerPorId(id).orElse(null);
        model.addAttribute("sede", s);
        return "nueva_sede";
    }

    @GetMapping("/web/sedes/eliminar/{id}")
    public String eliminarSede(@PathVariable Integer id) {
        sedeService.eliminarSede(id);
        return "redirect:/web/sedes";
    }

    @PostMapping("/web/sedes/guardar")
    public String guardarSede(@ModelAttribute("sede") Sede sede) {
        sedeService.guardarSede(sede);
        return "redirect:/web/sedes";
    }

    // ==========================================
    // SECCIÓN 4: PROVEEDORES
    // ==========================================
    @GetMapping("/web/proveedores")
    public String listarProveedores(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String contacto,
            Model model) {

        List<Proveedor> lista;
        if (nombre == null && contacto == null) {
            lista = proveedorRepository.findAll();
        } else {
            lista = proveedorRepository.buscarAvanzado(nombre, contacto);
        }
        model.addAttribute("listaProveedores", lista);
        return "proveedores";
    }

    @GetMapping("/web/proveedores/nuevo")
    public String formProveedor(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "nuevo_proveedor";
    }

    @GetMapping("/web/proveedores/editar/{id}")
    public String editarProveedor(@PathVariable Integer id, Model model) {
        Proveedor p = proveedorRepository.findById(id).orElse(null);
        model.addAttribute("proveedor", p);
        return "nuevo_proveedor";
    }

    @GetMapping("/web/proveedores/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id) {
        proveedorRepository.deleteById(id);
        return "redirect:/web/proveedores";
    }

    @PostMapping("/web/proveedores/guardar")
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedor) {
        proveedorRepository.save(proveedor);
        return "redirect:/web/proveedores";
    }

    // ================= 5. POS =================
    @GetMapping("/web/pos")
    public String pantallaPos(Model model) {
        model.addAttribute("listaProductos", productoService.obtenerTodos());
        model.addAttribute("listaClientes", clienteService.obtenerTodos());
        model.addAttribute("listaSedes", sedeService.obtenerTodas());
        return "pos";
    }

    // ================= 6. NUEVAS SECCIONES (HISTORIALES) =================

    @GetMapping("/web/inventario")
    public String listarInventario(Model model) {
        model.addAttribute("listaInventario", inventarioRepository.findAll());
        return "inventario"; // inventario.html
    }

    @GetMapping("/web/historial/compras")
    public String historialCompras(Model model) {
        model.addAttribute("listaCompras", compraRepository.findAll());
        return "historial_compras"; // historial_compras.html
    }

    @GetMapping("/web/historial/ventas") // Facturas
    public String historialVentas(Model model) {
        model.addAttribute("listaFacturas", facturaRepository.findAll());
        return "historial_ventas"; // historial_ventas.html
    }

    @GetMapping("/web/historial/pagos")
    public String historialPagos(Model model) {
        model.addAttribute("listaPagos", pagoRepository.findAll());
        return "historial_pagos"; // historial_pagos.html
    }
}
