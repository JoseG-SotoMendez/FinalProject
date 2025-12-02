package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT i.producto.nombre, SUM(i.cantidad) as totalVendido " +
            "FROM OrderItem i GROUP BY i.producto.nombre " +
            "ORDER BY totalVendido DESC LIMIT 5")
    List<Object[]> obtenerProductosMasVendidos();
}