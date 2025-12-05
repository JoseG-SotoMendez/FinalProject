package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    @Query("SELECT p FROM Promocion p WHERE " +
            "(:codigo IS NULL OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))) AND " +
            "(:tipo IS NULL OR p.tipoDescuento = :tipo)")
    List<Promocion> filtrar(@Param("codigo") String codigo, @Param("tipo") String tipo);
}