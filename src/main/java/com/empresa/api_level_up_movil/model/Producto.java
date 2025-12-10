package com.empresa.api_level_up_movil.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column()
    private String poster;

    @Column(nullable = false)
    private int precio;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String estado;

    @OneToMany(mappedBy = "producto")
    @JsonManagedReference("producto-detalle_pedidos")
    private List<DetallePedido> detallePedidos;

}
