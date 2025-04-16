package com.example.papeleriaDB.Repository;

import com.example.papeleriaDB.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Métodos de consulta personalizados
    List<Producto> findByNombreContaining(String nombre);
    List<Producto> findByProveedorIdProveedor(Integer idProveedor);
    List<Producto> findByStockLessThan(Integer cantidad);
}