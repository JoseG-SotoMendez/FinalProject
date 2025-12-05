package com.unicordoba.FinalProject.controller;

import com.unicordoba.FinalProject.dto.DashboardDTO;
import com.unicordoba.FinalProject.entity.*;
import com.unicordoba.FinalProject.repository.*;
import com.unicordoba.FinalProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PromocionRepository promocionRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("titulo", "Sistema POS - Unicórdoba");
        return "index";
    }

    /*
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

     */

    @GetMapping("/web/dashboard")
    public String dashboard(Model model) {
        DashboardDTO stats = new DashboardDTO();
        stats.setTotalVentasDia(pedidoRepository.contarVentasHoy());
        stats.setIngresosDia(pedidoRepository.sumarIngresosHoy());
        stats.setProductosBajoStock(inventarioRepository.contarProductosBajoStock());
        stats.setClientesRegistrados(clienteRepository.count());
        model.addAttribute("stats", stats);
        model.addAttribute("graficoVentas", pedidoRepository.obtenerVentasUltimosDias());
        model.addAttribute("graficoProductos", orderItemRepository.obtenerProductosMasVendidos());
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
    public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            productoService.eliminarProducto(id);
            redirectAttrs.addFlashAttribute("mensaje", "Producto eliminado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: Hay inventario o ventas de este producto.");
        }
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
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            clienteService.eliminarCliente(id);
            redirectAttrs.addFlashAttribute("mensaje", "Cliente eliminado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: El cliente tiene facturas asociadas.");
        }
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
    public String eliminarSede(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            sedeService.eliminarSede(id);
            redirectAttrs.addFlashAttribute("mensaje", "Sede eliminada.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: La sede tiene historial de movimientos.");
        }
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
    public String eliminarProveedor(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            proveedorRepository.deleteById(id);
            redirectAttrs.addFlashAttribute("mensaje", "Proveedor eliminado.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: Hay compras asociadas a este proveedor.");
        }
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
    public String listarInventario(
            @RequestParam(required = false) Integer sedeId,
            @RequestParam(required = false) String producto,
            Model model) {

        List<Inventario> lista = (sedeId == null && producto == null) ?
                inventarioRepository.findAll() : inventarioRepository.filtrarInventario(sedeId, producto);

        model.addAttribute("listaInventario", lista);
        model.addAttribute("sedes", sedeService.obtenerTodas()); // Para el filtro
        return "inventario";
    }

    @GetMapping("/web/inventario/editar/{id}")
    public String editarInventario(@PathVariable Integer id, Model model) {
        Inventario inv = inventarioRepository.findById(id).orElse(null);
        model.addAttribute("inventario", inv);
        return "editar_inventario"; // Vista nueva
    }

    @PostMapping("/web/inventario/guardar")
    public String guardarInventario(@ModelAttribute("inventario") Inventario inventario, RedirectAttributes ra) {
        // Ojo: Solo guardamos cantidad y unidad para no romper integridad de producto/sede
        Inventario existente = inventarioRepository.findById(inventario.getInventarioId()).orElse(null);
        if(existente != null) {
            existente.setCantidad(inventario.getCantidad());
            existente.setUnidad(inventario.getUnidad());
            inventarioRepository.save(existente);
            ra.addFlashAttribute("mensaje", "Inventario actualizado manualmente.");
        }
        return "redirect:/web/inventario";
    }

    @GetMapping("/web/inventario/eliminar/{id}")
    public String eliminarInventario(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            inventarioRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Registro de inventario eliminado.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error al eliminar.");
        }
        return "redirect:/web/inventario";
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

    @GetMapping("/web/historial/ventas/eliminar/{id}")
    public String eliminarFactura(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            facturaRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Factura eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar la factura.");
        }
        return "redirect:/web/historial/ventas";
    }

    @GetMapping("/web/historial/pagos/eliminar/{id}")
    public String eliminarPago(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            pagoRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Pago eliminado.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar el pago.");
        }
        return "redirect:/web/historial/pagos";
    }

    @GetMapping("/web/historial/compras/eliminar/{id}")
    public String eliminarCompra(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            compraRepository.deleteById(id);
            ra.addFlashAttribute("mensaje", "Compra eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se puede eliminar la compra (tiene items asociados).");
        }
        return "redirect:/web/historial/compras";
    }

    @GetMapping("/web/promociones")
    public String listarPromociones(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String tipo,
            Model model) {
        List<Promocion> lista = (codigo == null && tipo == null) ?
                promocionService.obtenerTodas() : promocionRepository.filtrar(codigo, tipo);
        model.addAttribute("listaPromociones", lista);
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

    @PostMapping("/web/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/web/usuarios";
    }

    @GetMapping("/web/usuarios")
    public String listarUsuarios(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String rol,
            Model model) {
        List<Usuario> lista = (username == null && rol == null) ?
                usuarioService.obtenerTodos() : usuarioRepository.filtrar(username, rol);
        model.addAttribute("listaUsuarios", lista);
        return "usuarios";
    }

    @GetMapping("/web/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        try {
            usuarioService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: El usuario tiene ventas o registros asociados.");
        }
        return "redirect:/web/usuarios";
    }

}
