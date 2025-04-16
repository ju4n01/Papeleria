package com.example.papeleriaDB.Controller;

import com.example.papeleriaDB.Model.DetalleVenta;
import com.example.papeleriaDB.Model.Venta;
import com.example.papeleriaDB.Service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        return ResponseEntity.ok(ventaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Integer id) {
        return ventaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.save(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        return ventaService.findById(id)
                .map(ventaExistente -> {
                    venta.setIdVenta(id);
                    return ResponseEntity.ok(ventaService.save(venta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Integer id) {
        return ventaService.findById(id)
                .map(venta -> {
                    ventaService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Venta>> findByFecha(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecha) {
        return ResponseEntity.ok(ventaService.findByFecha(fecha));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venta>> findByClienteId(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(ventaService.findByClienteId(idCliente));
    }

    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<List<Venta>> findByEmpleadoId(@PathVariable Integer idEmpleado) {
        return ResponseEntity.ok(ventaService.findByEmpleadoId(idEmpleado));
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> getDetallesByVentaId(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.findDetallesByVentaId(id));
    }
}