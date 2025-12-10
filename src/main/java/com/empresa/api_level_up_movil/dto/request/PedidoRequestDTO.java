package com.empresa.api_level_up_movil.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDTO {

    private Long user_id;
    private int numero_pedido;
    private String estado;
    private List<PagoRequestDTO> pagos;
    private List<DetallePedidoRequestDTO> detalles;
}
