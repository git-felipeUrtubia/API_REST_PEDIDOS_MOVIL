package com.empresa.api_level_up_movil.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
    private LocalDate fecha_registro;

    @Data
    public static class UpdateUser {

        private String nombre;
        private String apellido;
        private String email;
        private String password;

    }

}
