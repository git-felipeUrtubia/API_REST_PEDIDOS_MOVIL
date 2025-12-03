package com.empresa.api_level_up_movil.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;


@JsonPropertyOrder({
        "id_pedido",
        "id_cliente",
        "numero_pedido",
        "detalle_pedidos"
})
@Data
public class PedidoResponseDTO {

    private Long id_pedido;
    private Long id_cliente;
    private int numero_pedido;
    private List<DetallePedidoResponseDTO> detalle_pedidos;

}
