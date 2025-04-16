package com.example.papeleriaDB.Repository;

import com.example.papeleriaDB.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreContaining(String nombre);
    Optional<Cliente> findByCedula(String cedula);
}