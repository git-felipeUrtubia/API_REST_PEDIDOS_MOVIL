package com.empresa.api_level_up_movil.repository;

import com.empresa.api_level_up_movil.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    boolean existsPedidoByEstado(String estado);

    void deleteByEstado(String estado);
}
