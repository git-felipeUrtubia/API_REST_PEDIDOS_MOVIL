package com.empresa.api_level_up_movil.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;


@JsonPropertyOrder({
        "id_pedido",
        "id_user",
        "numero_pedido",
        "estado",
        "pagos",
        "detalle_pedidos"
})
@Data
public class PedidoResponseDTO {

    private Long id_pedido;
    private Long id_user;
    private int numero_pedido;
    private String estado;
    private List<PagoResponseDTO> pagos;
    private List<DetallePedidoResponseDTO> detalle_pedidos;

    @JsonPropertyOrder({
            "id_user",
            "numero_pedido",
            "estado",
            "pagos",
            "detalle_pedidos"
    })
    @Data
    public static class ById {
        private Long id_user;
        private int numero_pedido;
        private String estado;
        private List<PagoResponseDTO> pagos;
        private List<DetallePedidoResponseDTO.ById> detalle_pedidos;
    }

}
