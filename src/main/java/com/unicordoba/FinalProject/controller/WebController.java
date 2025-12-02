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
import java.time.LocalDate;
import java.util.List;

@Controller
public class WebController {

    @Autowired private ProductoService productoService;
    @Autowired private ClienteService clienteService;
    @Autowired private SedeService sedeService;
    @Autowired private CompraService compraService;
    @Autowired private PromocionService promocionService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private InventarioRepository inventarioRepository;
    @Autowired private SedeRepository sedeRepository;
    @Autowired private FacturaRepository facturaRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private PagoRepository pagoRepository;

    @Autowired private com.unicordoba.FinalProject.repository.ProveedorRepository proveedorRepository;

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

        List<Object[]> ventasData = pedidoRepository.obtenerVentasUltimosDias();
        model.addAttribute("graficoVentas", ventasData);

        List<Object[]> productosTop = orderItemRepository.obtenerProductosMasVendidos();
        model.addAttribute("graficoProductos", productosTop);

        return "dashboard";
    }

    @GetMapping("/web/compras/nueva")
    public String formNuevaCompra(Model model) {
        model.addAttribute("listaProveedores", proveedorRepository.findAll());
        model.addAttribute("listaSedes", sedeService.obtenerTodas());
        model.addAttribute("listaProductos", productoService.obtenerTodos());
        return "nueva_compra"; // Vamos a crear este archivo ahora
    }

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

    @GetMapping("/web/productos/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto p = productoService.obtenerPorId(id).orElse(null);
        model.addAttribute("producto", p);
        return "nuevo_producto";
    }

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

    @GetMapping("/web/pos")
    public String pantallaPos(Model model) {
        model.addAttribute("listaProductos", productoService.obtenerTodos());
        model.addAttribute("listaClientes", clienteService.obtenerTodos());
        model.addAttribute("listaSedes", sedeService.obtenerTodas());
        // Agregamos las promociones activas
        model.addAttribute("listaPromociones", promocionService.obtenerTodas());
        return "pos";
    }

    @GetMapping("/web/inventario")
    public String listarInventario(Model model) {
        model.addAttribute("listaInventario", inventarioRepository.findAll());
        return "inventario"; // inventario.html
    }

    @GetMapping("/web/historial/ventas")
    public String historialVentas(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin,
            @RequestParam(required = false) String estado,
            Model model) {

        List<Factura> lista;
        if (inicio == null && fin == null && estado == null) {
            lista = facturaRepository.findAll();
        } else {
            lista = facturaRepository.filtrarFacturas(inicio, fin, estado);
        }
        model.addAttribute("listaFacturas", lista);
        return "historial_ventas";
    }

    @GetMapping("/web/historial/compras")
    public String historialCompras(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin,
            @RequestParam(required = false) Integer proveedorId,
            Model model) {

        List<Compra> lista;
        if (inicio == null && fin == null && proveedorId == null) {
            lista = compraRepository.findAll();
        } else {
            lista = compraRepository.filtrarCompras(inicio, fin, proveedorId);
        }
        model.addAttribute("listaCompras", lista);
        model.addAttribute("proveedores", proveedorRepository.findAll()); // Para el select
        return "historial_compras";
    }

    // --- HISTORIAL PAGOS CON FILTRO ---
    @GetMapping("/web/historial/pagos")
    public String historialPagos(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fin,
            @RequestParam(required = false) String metodo,
            Model model) {

        List<Pago> lista;
        if (inicio == null && fin == null && metodo == null) {
            lista = pagoRepository.findAll();
        } else {
            lista = pagoRepository.filtrarPagos(inicio, fin, metodo);
        }
        model.addAttribute("listaPagos", lista);
        return "historial_pagos";
    }

    @GetMapping("/web/promociones")
    public String listarPromociones(Model model) {
        model.addAttribute("listaPromociones", promocionService.obtenerTodas());
        return "promociones";
    }

    @GetMapping("/web/promociones/nuevo")
    public String formPromocion(Model model) {
        model.addAttribute("promocion", new Promocion());
        return "nueva_promocion";
    }

    @GetMapping("/web/promociones/editar/{id}")
    public String editarPromocion(@PathVariable Integer id, Model model) {
        Promocion p = promocionService.obtenerPorId(id).orElse(null);
        model.addAttribute("promocion", p);
        return "nueva_promocion";
    }

    @GetMapping("/web/promociones/eliminar/{id}")
    public String eliminarPromocion(@PathVariable Integer id) {
        promocionService.eliminar(id);
        return "redirect:/web/promociones";
    }

    @PostMapping("/web/promociones/guardar")
    public String guardarPromocion(@ModelAttribute("promocion") Promocion promocion) {
        promocionService.guardar(promocion);
        return "redirect:/web/promociones";
    }

    @GetMapping("/web/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.obtenerTodos());
        return "usuarios"; // usuarios.html
    }

    @GetMapping("/web/usuarios/nuevo")
    public String formUsuario(Model model) {
        Usuario u = new Usuario();
        u.setActivo(true); // Activo por defecto
        model.addAttribute("usuario", u);
        return "nuevo_usuario";
    }

    @GetMapping("/web/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Usuario u = usuarioService.obtenerPorId(id).orElse(null);
        model.addAttribute("usuario", u);
        return "nuevo_usuario";
    }

    @GetMapping("/web/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/web/usuarios";
    }

    @PostMapping("/web/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/web/usuarios";
    }

}
