package com.unicordoba.FinalProject.service;

import com.unicordoba.FinalProject.dto.PagoDTO;
import com.unicordoba.FinalProject.entity.Factura;
import com.unicordoba.FinalProject.entity.Pago;
import com.unicordoba.FinalProject.entity.Pedido;
import com.unicordoba.FinalProject.repository.FacturaRepository;
import com.unicordoba.FinalProject.repository.PagoRepository;
import com.unicordoba.FinalProject.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FacturacionService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private FacturaRepository facturaRepository;

    @Transactional
    public Factura registrarPago(PagoDTO pagoDTO) {
        // 1. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pagoDTO.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if ("PAGADO".equals(pedido.getEstado())) {
            throw new RuntimeException("Este pedido ya fue pagado anteriormente.");
        }

        // 2. Registrar el Pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago("APROBADO");
        pago.setReferenciaTransaccion(UUID.randomUUID().toString()); // Simulamos un código de banco
        pagoRepository.save(pago);

        // 3. Actualizar estado del Pedido
        // (Aquí podrías validar si el monto pago >= total pedido, pero asumiremos que sí)
        pedido.setEstado("PAGADO");
        pedidoRepository.save(pedido);

        // 4. Generar la Factura Automáticamente
        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setCliente(pedido.getCliente());
        factura.setValorTotal(pedido.getTotal());
        factura.setEstado("VIGENTE");
        // Generamos un número de factura simple (Ej: FAC-12345)
        factura.setNumeroFactura("FAC-" + System.currentTimeMillis());

        return facturaRepository.save(factura);
    }
}