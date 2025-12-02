package com.unicordoba.FinalProject.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {
    private Integer clienteId;
    private Integer sedeId;
    private Integer usuarioId; // El cajero
    private String tipo; // MESA, DOMICILIO
    private List<DetallePedidoDTO> items; // La lista de productos
}