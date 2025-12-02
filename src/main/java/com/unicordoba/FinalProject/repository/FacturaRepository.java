package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

    @Query("SELECT f FROM Factura f WHERE " +
            "(:fechaInicio IS NULL OR DATE(f.fechaEmision) >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR DATE(f.fechaEmision) <= :fechaFin) AND " +
            "(:estado IS NULL OR f.estado = :estado)")
    List<Factura> filtrarFacturas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            @Param("estado") String estado);
}