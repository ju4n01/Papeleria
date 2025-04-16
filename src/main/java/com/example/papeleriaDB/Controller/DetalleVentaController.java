package com.example.papeleriaDB.Controller;

import com.example.papeleriaDB.Model.DetalleVenta;
import com.example.papeleriaDB.Service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles-venta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    @Autowired
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    public ResponseEntity<List<DetalleVenta>> getAllDetallesVenta() {
        return ResponseEntity.ok(detalleVentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> getDetalleVentaById(@PathVariable Integer id) {
        return detalleVentaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DetalleVenta> createDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        DetalleVenta nuevoDetalle = detalleVentaService.save(detalleVenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> updateDetalleVenta(@PathVariable Integer id, @RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.findById(id)
                .map(detalleExistente -> {
                    detalleVenta.setId_detalle(id);
                    return ResponseEntity.ok(detalleVentaService.save(detalleVenta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetalleVenta(@PathVariable Integer id) {
        return detalleVentaService.findById(id)
                .map(detalle -> {
                    detalleVentaService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<List<DetalleVenta>> findByVentaId(@PathVariable Integer idVenta) {
        return ResponseEntity.ok(detalleVentaService.findByVentaId(idVenta));
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<DetalleVenta>> findByProductoId(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(detalleVentaService.findByProductoId(idProducto));
    }
}
