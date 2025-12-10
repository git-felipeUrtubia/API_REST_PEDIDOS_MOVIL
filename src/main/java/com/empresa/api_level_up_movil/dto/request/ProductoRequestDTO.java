package com.empresa.api_level_up_movil.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductoRequestDTO {

    private String nombre;
    private String descripcion;
    private MultipartFile poster;
    private int precio;
    private String categoria;
    private int stock;
    private String estado;

    @Data
    public static class ProductoUpdate {
        private Long id_producto;
        private String nombre;
        private String descripcion;
        private MultipartFile poster;
        private int precio;
        private String categoria;
        private int stock;
    }

}
