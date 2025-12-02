package com.unicordoba.FinalProject.repository;

import com.unicordoba.FinalProject.entity.Inventario;
import com.unicordoba.FinalProject.entity.Producto;
import com.unicordoba.FinalProject.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    // SQL automático: SELECT * FROM inventario WHERE sede_id = ? AND producto_id = ?
    Optional<Inventario> findBySedeAndProducto(Sede sede, Producto producto);
}