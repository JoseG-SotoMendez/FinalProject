package com.unicordoba.FinalProject.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardDTO {
    private Long totalVentasDia;      // Cuántos pedidos hoy
    private BigDecimal ingresosDia;   // Cuánto dinero hoy
    private Long productosBajoStock;  // Alerta de inventario
    private Long clientesRegistrados;
}