package com.example.papeleriaDB.Repository;

import com.example.papeleriaDB.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    List<Venta> findByFecha(Date fecha);
    List<Venta> findByClienteIdCliente(Integer idCliente);
    List<Venta> findByEmpleadoIdEmpleado(Integer idEmpleado);
}
