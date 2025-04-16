package com.example.papeleriaDB.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpleado;

    @Column(length = 100)
    private String nombre;

    @Column(length = 50)
    private String cargo;

    @Column(length = 50)
    private String telefono;

    @OneToMany(mappedBy = "empleado")
    private List<Venta> ventas;
}
