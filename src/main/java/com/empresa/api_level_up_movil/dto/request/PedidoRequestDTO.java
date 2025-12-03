package com.empresa.api_level_up_movil.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {

    private int numero_pedido;
    private Long cliente_id;
    private List<DetallePedidoRequestDTO> detalles;


}
