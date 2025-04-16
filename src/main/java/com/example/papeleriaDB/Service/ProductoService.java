package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.Producto;
import com.example.papeleriaDB.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        try {
            return productoRepository.findAll();
        } catch (DataAccessException e) {
            // Registrar el error (aquí podrías usar un logger)
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
            // Retornar una lista vacía o lanzar una excepción personalizada
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findById(Integer id) {
        if (id == null) {
            System.err.println("El ID del producto no puede ser nulo");
            return Optional.empty();
        }

        try {
            return productoRepository.findById(id);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar el producto con ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    public Producto save(Producto producto) {
        try {
            if (producto == null) {
                throw new IllegalArgumentException("El producto no puede ser nulo");
            }

            // Validaciones de negocio
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
            }

            if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
                throw new IllegalArgumentException("El precio del producto debe ser mayor que cero");
            }

            // Si todo está bien, guardar el producto
            return productoRepository.save(producto);
        } catch (DataAccessException e) {
            System.err.println("Error al guardar el producto: " + e.getMessage());
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del producto no puede ser nulo");
        }

        try {
            // Verificar primero si el producto existe
            if (!productoRepository.existsById(id)) {
                throw new EmptyResultDataAccessException("No se encontró el producto con ID " + id, 1);
            }
            productoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            System.err.println("Error de acceso a datos al eliminar el producto con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error al eliminar el producto", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> findByNombreContaining(String nombre) {
        if (nombre == null) {
            System.err.println("El nombre de búsqueda no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return productoRepository.findByNombreContaining(nombre);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar productos por nombre '" + nombre + "': " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> findByProveedorId(Integer idProveedor) {
        if (idProveedor == null) {
            System.err.println("El ID del proveedor no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return productoRepository.findByProveedorIdProveedor(idProveedor);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar productos por ID de proveedor " + idProveedor + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Producto> findByStockBajo(Integer cantidad) {
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser un valor positivo");
        }

        try {
            return productoRepository.findByStockLessThan(cantidad);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar productos con stock bajo " + cantidad + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}