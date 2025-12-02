package com.unicordoba.FinalProject.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagoDTO {
    private Integer pedidoId;
    private BigDecimal monto;
    private String metodoPago;
}