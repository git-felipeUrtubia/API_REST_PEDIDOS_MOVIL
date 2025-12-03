package com.empresa.api_level_up_movil.dto.request;

import lombok.Data;

@Data
public class ProductoRequestDTO {

    private String nombre;
    private String descripcion;
    private String poster;
    private int precio;
    private String categoria;
    private int stock;

}
