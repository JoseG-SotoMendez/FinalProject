package com.unicordoba.FinalProject.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {
    private Integer clienteId;
    private Integer sedeId;
    private Integer usuarioId;
    private String tipo;
    private List<DetallePedidoDTO> items;
}