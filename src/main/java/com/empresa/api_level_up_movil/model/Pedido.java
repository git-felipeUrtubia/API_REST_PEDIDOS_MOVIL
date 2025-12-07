package com.empresa.api_level_up_movil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @Column(nullable = false)
    private int numero_pedido;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user_fk", nullable = false)
    @JsonBackReference("user-pedidos")
    private User user;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference("pedido-detalle_pedidos")
    private List<DetallePedido> detalle_pedidos;

}
