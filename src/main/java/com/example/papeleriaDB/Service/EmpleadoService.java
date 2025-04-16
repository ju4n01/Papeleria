package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.Empleado;
import com.example.papeleriaDB.Repository.EmpleadoRepository;
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
public class EmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        try {
            return empleadoRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error al obtener todos los empleados", e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Empleado> findById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            return empleadoRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error al buscar empleado con ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Transactional
    public Empleado save(Empleado empleado) {
        if (empleado == null) {
            logger.error("Intento de guardar un empleado nulo");
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }

        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            logger.error("Nombre de empleado inválido");
            throw new IllegalArgumentException("El nombre del empleado no puede estar vacío");
        }

        if (empleado.getCargo() == null || empleado.getCargo().trim().isEmpty()) {
            logger.error("Cargo de empleado inválido");
            throw new IllegalArgumentException("El cargo del empleado no puede estar vacío");
        }

        try {
            return empleadoRepository.save(empleado);
        } catch (DataAccessException e) {
            logger.error("Error al guardar empleado: {}", empleado, e);
            throw new RuntimeException("Error al guardar el empleado", e);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            empleadoRepository.deleteById(id);
            logger.info("Empleado con ID: {} eliminado exitosamente", id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("No se encontró empleado con ID: {} para eliminar", id, e);
            throw new IllegalArgumentException("El empleado con ID: " + id + " no existe", e);
        } catch (DataAccessException e) {
            logger.error("Error al eliminar empleado con ID: {}", id, e);
            throw new RuntimeException("Error al eliminar el empleado", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Empleado> findByNombreContaining(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            logger.error("Nombre de búsqueda inválido");
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }

        try {
            return empleadoRepository.findByNombreContaining(nombre);
        } catch (DataAccessException e) {
            logger.error("Error al buscar empleados por nombre: {}", nombre, e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<Empleado> findByCargo(String cargo) {
        if (cargo == null || cargo.trim().isEmpty()) {
            logger.error("Cargo de búsqueda inválido");
            throw new IllegalArgumentException("El cargo de búsqueda no puede estar vacío");
        }

        try {
            return empleadoRepository.findByCargo(cargo);
        } catch (DataAccessException e) {
            logger.error("Error al buscar empleados por cargo: {}", cargo, e);
            return Collections.emptyList();
        }
    }
}