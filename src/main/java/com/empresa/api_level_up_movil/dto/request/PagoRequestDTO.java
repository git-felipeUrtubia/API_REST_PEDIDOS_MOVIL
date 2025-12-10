package com.empresa.api_level_up_movil.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;


@Data
public class PagoRequestDTO {
    private int numero_pago;
    private double subtotal;
    private double iva;
    private int monto;
    private String tipo;
    private LocalDate fecha_registro;
}
