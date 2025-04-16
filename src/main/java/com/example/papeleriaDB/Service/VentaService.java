package com.example.papeleriaDB.Service;

import com.example.papeleriaDB.Model.DetalleVenta;
import com.example.papeleriaDB.Model.Producto;
import com.example.papeleriaDB.Model.Venta;
import com.example.papeleriaDB.Repository.DetalleVentaRepository;
import com.example.papeleriaDB.Repository.ProductoRepository;
import com.example.papeleriaDB.Repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository,
                         DetalleVentaRepository detalleVentaRepository,
                         ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Venta> findAll() {
        try {
            return ventaRepository.findAll();
        } catch (DataAccessException e) {
            System.err.println("Error al obtener todas las ventas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Optional<Venta> findById(Integer id) {
        if (id == null) {
            System.err.println("El ID de la venta no puede ser nulo");
            return Optional.empty();
        }

        try {
            return ventaRepository.findById(id);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar la venta con ID " + id + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Transactional
    public Venta save(Venta venta) {
        try {
            if (venta == null) {
                throw new IllegalArgumentException("La venta no puede ser nula");
            }

            // Validaciones de negocio
            if (venta.getCliente() == null || venta.getCliente().getIdCliente() == null) {
                throw new IllegalArgumentException("La venta debe tener un cliente asociado");
            }

            if (venta.getEmpleado() == null || venta.getEmpleado().getIdEmpleado() == null) {
                throw new IllegalArgumentException("La venta debe tener un empleado asociado");
            }

            if (venta.getFecha() == null) {
                // Establecer la fecha actual si no se proporciona
                venta.setFecha(new Date());
            }

            // Validar que la venta tenga detalles
            if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
                throw new IllegalArgumentException("La venta debe tener al menos un detalle");
            }

            // Guardar primero la venta
            Venta ventaGuardada = ventaRepository.save(venta);

            // Procesar cada detalle
            for (DetalleVenta detalle : venta.getDetalles()) {
                if (detalle.getProducto() == null || detalle.getProducto().getIdProducto() == null) {
                    throw new IllegalArgumentException("Cada detalle debe tener un producto asociado");
                }

                if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
                    throw new IllegalArgumentException("La cantidad de cada detalle debe ser mayor que cero");
                }

                // Verificar stock disponible
                Optional<Producto> optProducto = productoRepository.findById(detalle.getProducto().getIdProducto());
                if (optProducto.isPresent()) {
                    Producto producto = optProducto.get();
                    if (producto.getStock() < detalle.getCantidad()) {
                        throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
                    }

                    // Actualizar stock
                    producto.setStock(producto.getStock() - detalle.getCantidad());
                    productoRepository.save(producto);
                } else {
                    throw new IllegalArgumentException("El producto con ID " + detalle.getProducto().getIdProducto() + " no existe");
                }

                // Asignar la venta guardada al detalle
                detalle.setVenta(ventaGuardada);
                detalleVentaRepository.save(detalle);
            }

            return ventaGuardada;
        } catch (DataAccessException e) {
            System.err.println("Error de acceso a datos al guardar la venta: " + e.getMessage());
            throw new RuntimeException("Error al guardar la venta: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la venta no puede ser nulo");
        }

        try {
            // Verificar primero si la venta existe
            Optional<Venta> ventaOpt = ventaRepository.findById(id);
            if (!ventaOpt.isPresent()) {
                throw new EmptyResultDataAccessException("No se encontró la venta con ID " + id, 1);
            }

            // Recuperar los detalles de la venta para restaurar el stock
            List<DetalleVenta> detalles = detalleVentaRepository.findByVentaIdVenta(id);
            for (DetalleVenta detalle : detalles) {
                Optional<Producto> productoOpt = productoRepository.findById(detalle.getProducto().getIdProducto());
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    // Devolver los productos al stock
                    producto.setStock(producto.getStock() + detalle.getCantidad());
                    productoRepository.save(producto);
                }
            }

            // Eliminar la venta (los detalles se eliminarán en cascada si está configurado así)
            ventaRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            System.err.println("Error de acceso a datos al eliminar la venta con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Error al eliminar la venta", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Venta> findByFecha(Date fecha) {
        if (fecha == null) {
            System.err.println("La fecha de búsqueda no puede ser nula");
            return new ArrayList<>();
        }

        try {
            return ventaRepository.findByFecha(fecha);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar ventas por fecha '" + fecha + "': " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Venta> findByClienteId(Integer idCliente) {
        if (idCliente == null) {
            System.err.println("El ID del cliente no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return ventaRepository.findByClienteIdCliente(idCliente);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar ventas por ID de cliente " + idCliente + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Venta> findByEmpleadoId(Integer idEmpleado) {
        if (idEmpleado == null) {
            System.err.println("El ID del empleado no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return ventaRepository.findByEmpleadoIdEmpleado(idEmpleado);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar ventas por ID de empleado " + idEmpleado + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<DetalleVenta> findDetallesByVentaId(Integer idVenta) {
        if (idVenta == null) {
            System.err.println("El ID de la venta no puede ser nulo");
            return new ArrayList<>();
        }

        try {
            return detalleVentaRepository.findByVentaIdVenta(idVenta);
        } catch (DataAccessException e) {
            System.err.println("Error al buscar detalles por ID de venta " + idVenta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}