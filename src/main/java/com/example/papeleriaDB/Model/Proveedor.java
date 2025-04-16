package com.example.papeleriaDB.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;

    @Column(length = 100)
    private String nombre;

    @Column(length = 50)
    private String telefono;

    @Column(length = 100)
    private String correo;

    @Column(length = 150)
    private String direccion;

    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productos;
}
