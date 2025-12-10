package com.empresa.api_level_up_movil.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;

    @Column(nullable = false)
    private int numero_pago;

    @Column(nullable = false)
    private double subtotal;

    @Column(nullable = false)
    private double iva;

    @Column(nullable = false)
    private int monto;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private LocalDate fecha_registro;

    @ManyToOne()
    @JoinColumn(name = "id_pedido_fk")
    @JsonBackReference("pedido-pago")
    Pedido pedido;

}
