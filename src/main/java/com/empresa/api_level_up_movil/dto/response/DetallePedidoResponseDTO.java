package com.empresa.api_level_up_movil.dto.response;

import lombok.Data;

@Data
public class DetallePedidoResponseDTO {

    private int cant;
    private Long id_producto;

    @Data
    public  static class ById{
        private int cant;
        private Long id_producto;
        private String nombre;

    }
}
