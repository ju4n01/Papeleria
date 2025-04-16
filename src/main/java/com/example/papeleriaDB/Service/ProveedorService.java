package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.Proveedor;
import com.example.papeleriaDB.Repository.ProveedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorService.class);
    private final ProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<Proveedor> findAll() {
        try {
            return proveedorRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error al obtener todos los proveedores", e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Proveedor> findById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            return proveedorRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error al buscar proveedor con ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Transactional
    public Proveedor save(Proveedor proveedor) {
        if (proveedor == null) {
            logger.error("Intento de guardar un proveedor nulo");
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }

        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            logger.error("Nombre de proveedor inválido");
            throw new IllegalArgumentException("El nombre del proveedor no puede estar vacío");
        }

        if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
            logger.error("Teléfono de proveedor inválido");
            throw new IllegalArgumentException("El teléfono del proveedor no puede estar vacío");
        }

        if (proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty()) {
            logger.error("Dirección de proveedor inválida");
            throw new IllegalArgumentException("La dirección del proveedor no puede estar vacía");
        }

        try {
            return proveedorRepository.save(proveedor);
        } catch (DataAccessException e) {
            logger.error("Error al guardar proveedor: {}", proveedor, e);
            throw new RuntimeException("Error al guardar el proveedor", e);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            proveedorRepository.deleteById(id);
            logger.info("Proveedor con ID: {} eliminado exitosamente", id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("No se encontró proveedor con ID: {} para eliminar", id, e);
            throw new IllegalArgumentException("El proveedor con ID: " + id + " no existe", e);
        } catch (DataAccessException e) {
            logger.error("Error al eliminar proveedor con ID: {}", id, e);
            throw new RuntimeException("Error al eliminar el proveedor", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Proveedor> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            logger.error("Nombre de búsqueda inválido");
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }

        try {
            return proveedorRepository.findByNombreContaining(nombre);
        } catch (DataAccessException e) {
            logger.error("Error al buscar proveedores por nombre: {}", nombre, e);
            return Collections.emptyList();
        }
    }
}
