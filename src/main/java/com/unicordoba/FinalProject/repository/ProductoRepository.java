package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query("SELECT p FROM Producto p WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:minPrecio IS NULL OR p.precioBase >= :minPrecio) AND " +
            "(:maxPrecio IS NULL OR p.precioBase <= :maxPrecio) AND " +
            "(:activo IS NULL OR p.activo = :activo)")
    List<Producto> buscarProductosAvanzado(
            @Param("nombre") String nombre,
            @Param("minPrecio") BigDecimal minPrecio,
            @Param("maxPrecio") BigDecimal maxPrecio,
            @Param("activo") Boolean activo);
}