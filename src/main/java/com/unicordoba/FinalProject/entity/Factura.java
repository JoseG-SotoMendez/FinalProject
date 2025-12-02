package com.unicordoba.FinalProject.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Integer facturaId;

    // Relación 1 a 1 con Pedido (Un pedido tiene una factura)
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "numero_factura", nullable = false, length = 50)
    private String numeroFactura;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "xml_electronico", columnDefinition = "TEXT")
    private String xmlElectronico; // Para guardar el XML de la DIAN si es necesario

    @Column(length = 20)
    private String estado; // PAGADA, ANULADA

    @PrePersist
    public void prePersist() {
        this.fechaEmision = LocalDateTime.now();
    }
}