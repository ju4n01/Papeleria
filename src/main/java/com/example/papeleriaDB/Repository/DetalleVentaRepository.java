package com.example.papeleriaDB.Repository;

import com.example.papeleriaDB.Model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    List<DetalleVenta> findByVentaIdVenta(Integer idVenta);
    List<DetalleVenta> findByProductoIdProducto(Integer idProducto);
}
