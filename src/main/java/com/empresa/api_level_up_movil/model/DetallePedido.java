package com.empresa.api_level_up_movil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_pedido;

    @Column(nullable = false)
    private int cant;

    @Column(nullable = false)
    private int subtotal;

    @ManyToOne
    @JoinColumn(name = "id_pedido_fk", nullable = false)
    @JsonBackReference("pedido-detalle_pedidos")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto_fk", nullable = false)
    @JsonBackReference("producto-detalle_pedidos")
    private Producto producto;
}
