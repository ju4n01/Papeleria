package com.example.papeleriaDB.Repository;

import com.example.papeleriaDB.Model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByNombreContaining(String nombre);
    List<Empleado> findByCargo(String cargo);
}
