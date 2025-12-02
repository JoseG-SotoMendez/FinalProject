package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.dto.DashboardDTO;
import com.unicordoba.FinalProject.entity.Producto;
import com.unicordoba.FinalProject.repository.ClienteRepository;
import com.unicordoba.FinalProject.repository.InventarioRepository;
import com.unicordoba.FinalProject.repository.PedidoRepository;
import com.unicordoba.FinalProject.repository.ProductoRepository;
import com.unicordoba.FinalProject.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // ¡OJO! No es RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.unicordoba.FinalProject.entity.*;
import com.unicordoba.FinalProject.service.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller // Esto indica que devolvemos Vistas HTML
public class WebController {

    // --- INYECCIÓN DE SERVICIOS ---
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private ProductoService productoService;
    @Autowired private ClienteService clienteService;   // <--- Agrega esto
    @Autowired private SedeService sedeService;         // <--- Agrega esto
    @Autowired private CompraService compraService;     // (Para más adelante)
    // Nota: Necesitamos acceder al repositorio de proveedores para listarlos
    @Autowired private com.unicordoba.FinalProject.repository.ProveedorRepository proveedorRepository;

    // Cuando entres a http://localhost:8080/ (la raíz)
    @GetMapping("/")
    public String index(Model model) {
        // "model" es como una mochila donde metemos cosas para llevar al HTML
        model.addAttribute("titulo", "Bienvenido al Sistema POS - Unicórdoba");
        return "index"; // Esto busca un archivo llamado index.html
    }

    // Cuando entres a http://localhost:8080/productos
    @GetMapping("/web/productos")
    public String listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal minPrecio,
            @RequestParam(required = false) BigDecimal maxPrecio,
            @RequestParam(required = false) Boolean activo,
            Model model) {

        List<Producto> resultados;

        // Si todos son nulos, traemos todo (findAll)
        if (nombre == null && minPrecio == null && maxPrecio == null && activo == null) {
            resultados = productoService.obtenerTodos();
        } else {
            // Si hay algún filtro, usamos la consulta avanzada
            resultados = productoRepository.buscarProductosAvanzado(nombre, minPrecio, maxPrecio, activo);
        }

        model.addAttribute("listaProductos", resultados);
        return "productos";
    }

    // 1. Mostrar el formulario vacío
    @GetMapping("/web/productos/nuevo")
    public String mostrarFormularioProducto(Model model) {
        Producto producto = new Producto();
        producto.setActivo(true); // Por defecto que salga activo
        model.addAttribute("producto", producto);
        return "nuevo_producto"; // Buscaremos el archivo nuevo_producto.html
    }

    @PostMapping("/web/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoService.guardarProducto(producto);
        return "redirect:/web/productos";
    }

    @GetMapping("/web/dashboard")
    public String dashboard(Model model) {
        DashboardDTO stats = new DashboardDTO();
        stats.setTotalVentasDia(pedidoRepository.contarVentasHoy());
        stats.setIngresosDia(pedidoRepository.sumarIngresosHoy());
        stats.setProductosBajoStock(inventarioRepository.contarProductosBajoStock());
        stats.setClientesRegistrados(clienteRepository.count());

        model.addAttribute("stats", stats);
        return "dashboard"; // dashboard.html
    }

    // ==========================================
    // SECCIÓN 2: CLIENTES
    // ==========================================
    @GetMapping("/web/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.obtenerTodos());
        return "clientes"; // Archivo clientes.html
    }

    @GetMapping("/web/clientes/nuevo")
    public String formCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "nuevo_cliente"; // Archivo nuevo_cliente.html
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
    public String listarSedes(Model model) {
        model.addAttribute("listaSedes", sedeService.obtenerTodas());
        return "sedes"; // Archivo sedes.html
    }

    @GetMapping("/web/sedes/nuevo")
    public String formSede(Model model) {
        model.addAttribute("sede", new Sede());
        return "nueva_sede"; // Archivo nueva_sede.html
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
    public String listarProveedores(Model model) {
        model.addAttribute("listaProveedores", proveedorRepository.findAll());
        return "proveedores"; // Archivo proveedores.html
    }

    @GetMapping("/web/proveedores/nuevo")
    public String formProveedor(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "nuevo_proveedor"; // Archivo nuevo_proveedor.html
    }

    @PostMapping("/web/proveedores/guardar")
    public String guardarProveedor(@ModelAttribute("proveedor") Proveedor proveedor) {
        proveedorRepository.save(proveedor);
        return "redirect:/web/proveedores";
    }

    // ==========================================
    // SECCIÓN 5: POS (PUNTO DE VENTA)
    // ==========================================
    @GetMapping("/web/pos")
    public String pantallaPos(Model model) {
        // Cargamos todas las listas necesarias para los desplegables (selects)
        model.addAttribute("listaProductos", productoService.obtenerTodos());
        model.addAttribute("listaClientes", clienteService.obtenerTodos());
        model.addAttribute("listaSedes", sedeService.obtenerTodas());

        // (Opcional) Si quieres pre-seleccionar el usuario cajero, podrías pasarlo aquí
        return "pos"; // Busca el archivo pos.html
    }

}