package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Query("SELECT COUNT(p) FROM Pedido p WHERE DATE(p.fechaHora) = CURRENT_DATE")
    Long contarVentasHoy();

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE DATE(p.fechaHora) = CURRENT_DATE AND p.estado = 'PAGADO'")
    BigDecimal sumarIngresosHoy();

    @Query(value = "SELECT DATE(fecha_hora) as fecha, SUM(total) as total " +
            "FROM pedido WHERE estado = 'PAGADO' " +
            "GROUP BY DATE(fecha_hora) ORDER BY fecha DESC LIMIT 7", nativeQuery = true)
    List<Object[]> obtenerVentasUltimosDias();
}