package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {

    @Query("SELECT s FROM Sede s WHERE " +
            "(:ciudad IS NULL OR LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) AND " +
            "(:nombre IS NULL OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<Sede> buscarPorCiudadYNombre(@Param("ciudad") String ciudad, @Param("nombre") String nombre);
}