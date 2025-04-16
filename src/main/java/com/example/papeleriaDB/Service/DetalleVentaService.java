package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.DetalleVenta;
import com.example.papeleriaDB.Repository.DetalleVentaRepository;
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

// BUG: propiedad id, no existente, rrastrear el error :(


@Service
public class DetalleVentaService {

    private static final Logger logger = LoggerFactory.getLogger(DetalleVentaService.class);
    private final DetalleVentaRepository detalleVentaRepository;

    @Autowired
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Transactional(readOnly = true)
    public List<DetalleVenta> findAll() {
        try {
            return detalleVentaRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Error al obtener todos los detalles de venta", e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<DetalleVenta> findById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            return detalleVentaRepository.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error al buscar detalle de venta con ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Transactional
    public DetalleVenta save(DetalleVenta detalleVenta) {
        if (detalleVenta == null) {
            logger.error("Intento de guardar un detalle de venta nulo");
            throw new IllegalArgumentException("El detalle de venta no puede ser nulo");
        }

        //if (detalleVenta.getVentaId() == null) {
        if (detalleVenta.getVenta() == null) {
            logger.error("El detalle de venta no tiene una venta asociada");
            throw new IllegalArgumentException("El detalle de venta debe estar asociado a una venta");
        }

        //if (detalleVenta.getProductoId() == null) {
        if (detalleVenta.getProducto() == null) {
            logger.error("El detalle de venta no tiene un producto asociado");
            throw new IllegalArgumentException("El detalle de venta debe estar asociado a un producto");
        }

        if (detalleVenta.getCantidad() <= 0) {
            logger.error("Cantidad inválida en detalle de venta: {}", detalleVenta.getCantidad());
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        try {
            return detalleVentaRepository.save(detalleVenta);
        } catch (DataAccessException e) {
            logger.error("Error al guardar detalle de venta: {}", detalleVenta, e);
            throw new RuntimeException("Error al guardar el detalle de venta", e);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            logger.error("El ID proporcionado es nulo");
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        try {
            detalleVentaRepository.deleteById(id);
            logger.info("Detalle de venta con ID: {} eliminado exitosamente", id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("No se encontró detalle de venta con ID: {} para eliminar", id, e);
            throw new IllegalArgumentException("El detalle de venta con ID: " + id + " no existe", e);
        } catch (DataAccessException e) {
            logger.error("Error al eliminar detalle de venta con ID: {}", id, e);
            throw new RuntimeException("Error al eliminar el detalle de venta", e);
        }
    }

    @Transactional(readOnly = true)
    public List<DetalleVenta> findByVentaId(Integer idVenta) {
        if (idVenta == null) {
            logger.error("El ID de venta proporcionado es nulo");
            throw new IllegalArgumentException("El ID de venta no puede ser nulo");
        }

        try {
            return detalleVentaRepository.findByVentaIdVenta(idVenta);
        } catch (DataAccessException e) {
            logger.error("Error al buscar detalles de venta por ID de venta: {}", idVenta, e);
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public List<DetalleVenta> findByProductoId(Integer idProducto) {
        if (idProducto == null) {
            logger.error("El ID de producto proporcionado es nulo");
            throw new IllegalArgumentException("El ID de producto no puede ser nulo");
        }

        try {
            return detalleVentaRepository.findByProductoIdProducto(idProducto);
        } catch (DataAccessException e) {
            logger.error("Error al buscar detalles de venta por ID de producto: {}", idProducto, e);
            return Collections.emptyList();
        }
    }
}