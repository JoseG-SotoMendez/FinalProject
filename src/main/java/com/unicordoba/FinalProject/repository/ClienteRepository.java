package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Consulta SQL Pro: Busca coincidencias parciales ignorando mayúsculas/minúsculas
    @Query("SELECT c FROM Cliente c WHERE " +
            "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%'))")
    List<Cliente> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("email") String email,
            @Param("telefono") String telefono);
}