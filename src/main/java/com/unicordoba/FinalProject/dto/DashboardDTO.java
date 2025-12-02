package com.unicordoba.FinalProject.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardDTO {
    private Long totalVentasDia;
    private BigDecimal ingresosDia;
    private Long productosBajoStock;
    private Long clientesRegistrados;
}