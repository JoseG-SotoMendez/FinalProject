package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    @Query("SELECT p FROM Proveedor p WHERE " +
            "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:contacto IS NULL OR LOWER(p.contacto) LIKE LOWER(CONCAT('%', :contacto, '%')))")
    List<Proveedor> buscarAvanzado(@Param("nombre") String nombre, @Param("contacto") String contacto);
}