package com.empresa.api_level_up_movil.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonPropertyOrder({
        "id_producto",
        "nombre",
        "descripcion",
        "poster",
        "precio",
        "categoria",
        "stock"
})
@Data
public class ProductoResponseDTO {

    private Long id_producto;
    private String nombre;
    private String descripcion;
    private String poster;
    private int precio;
    private String categoria;
    private int stock;

}
