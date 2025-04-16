package com.example.papeleriaDB.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    @Column(length = 100)
    private String nombre;

    @Column(length = 50)
    private String cedula;

    @Column(length = 50)
    private String telefono;

    @Column(length = 100)
    private String correo;

    @OneToMany(mappedBy = "cliente")
    private List<Venta> ventas;
}