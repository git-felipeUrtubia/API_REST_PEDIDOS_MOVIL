package com.empresa.api_level_up_movil.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private Long id_user;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private LocalDate fecha_registro;

}
