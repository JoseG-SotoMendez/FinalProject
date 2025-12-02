package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @Query("SELECT c FROM Compra c WHERE " +
            "(:fechaInicio IS NULL OR DATE(c.fecha) >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR DATE(c.fecha) <= :fechaFin) AND " +
            "(:proveedorId IS NULL OR c.proveedor.proveedorId = :proveedorId)")
    List<Compra> filtrarCompras(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("proveedorId") Integer proveedorId);
}