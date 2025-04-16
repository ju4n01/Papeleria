package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.Cliente;
import com.example.papeleriaDB.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        try {
            return clienteRepository.findAll();
        } catch (DataAccessException e) {
            System.err.println("Error al obtener todos los clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Integer id) {
        if (id == null) {
            System.err.println("El ID del cliente no puede ser nulo");
            return Optional.empty();
        }

        try {
            return clienteRepository.findById(id);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar el cliente con ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            if (cliente == null) {
                throw new IllegalArgumentException("El cliente no puede ser nulo");
            }

            // Validaciones de negocio
            if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente no puede estar vacío");
            }

            if (cliente.getCedula() == null || cliente.getCedula().trim().isEmpty()) {
                throw new IllegalArgumentException("La cédula del cliente no puede estar vacía");
            }

            // Verificar si ya existe un cliente con la misma cédula (solo para clientes nuevos)
            if (cliente.getIdCliente() == null) {
                Optional<Cliente> clienteExistente = clienteRepository.findByCedula(cliente.getCedula());
                if (clienteExistente.isPresent()) {
                    throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cliente.getCedula());
                }
            }

            // Si todo está bien, guardar el cliente
            return clienteRepository.save(cliente);
        } catch (DataAccessException e) {
            System.err.println("Error al guardar el cliente: " + e.getMessage());
            throw new RuntimeException("Error al guardar el cliente: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del cliente no puede ser nulo");
        }

        try {
            // Verificar primero si el cliente existe
            if (!clienteRepository.existsById(id)) {
                throw new EmptyResultDataAccessException("No se encontró el cliente con ID " + id, 1);
            }
            clienteRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            System.err.println("Error de acceso a datos al eliminar el cliente con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error al eliminar el cliente", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> findByNombreContaining(String nombre) {
        if (nombre == null) {
            System.err.println("El nombre de búsqueda no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return clienteRepository.findByNombreContaining(nombre);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar clientes por nombre '" + nombre + "': " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            System.err.println("La cédula de búsqueda no puede ser nula o vacía");
            return Optional.empty();
        }

        try {
            return clienteRepository.findByCedula(cedula);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar cliente por cédula '" + cedula + "': " + e.getMessage());
            return Optional.empty();
        }
    }
}