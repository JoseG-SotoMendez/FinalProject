package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // En PedidoRepository.java
    @Query("SELECT COUNT(p) FROM Pedido p WHERE DATE(p.fechaHora) = CURRENT_DATE")
    Long contarVentasHoy();

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Pedido p WHERE DATE(p.fechaHora) = CURRENT_DATE AND p.estado = 'PAGADO'")
    BigDecimal sumarIngresosHoy();
}